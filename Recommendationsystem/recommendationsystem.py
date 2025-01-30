from flask import Flask, request, jsonify
import tensorflow as tf
from tensorflow.keras.preprocessing.sequence import pad_sequences
import numpy as np
import pickle
import mysql.connector
import os
import pandas as pd
from scipy.sparse import csr_matrix
from scipy.sparse.linalg import svds

import string
app = Flask(__name__)

MODEL_PATH = "C:/Users/Admin/model_with_GRU_data2.h5"
TOKENIZER_PATH = "C:/Users/Admin/tokenizer_data2.pickle"

model = tf.keras.models.load_model(MODEL_PATH)
with open(TOKENIZER_PATH, 'rb') as handle:
    tokenizer = pickle.load(handle)
def predict_sentiment(text, model, tokenizer, max_length=128):
    text = text.lower()
    text = remove_punctuation(text)
    sequence = tokenizer.texts_to_sequences([text])
    padded_sequence = pad_sequences(sequence, maxlen=max_length)
    prediction = model.predict(padded_sequence)
    sentiment_score = int(np.argmax(prediction, axis=1)[0] + 1)
    return sentiment_score
def remove_punctuation(text):
    text = "".join([c for c in text if c not in string.punctuation])  
    text = text.replace("\n", " ") 
    text = text.replace("\t", " ")
    return text

@app.route('/sentiment', methods=['POST'])
def predict():
    data = request.get_json()
    text = data.get("text", "")
    if not text:
        return jsonify({"error": "No text provided"}), 400
    predicted_sentiment = predict_sentiment(text, model, tokenizer)
    return jsonify(predicted_sentiment)

def get_db_connection():
    connection = mysql.connector.connect(
        host='localhost',    
        user='root',
        password='123456',
        database='doan' 
    )
    return connection

def fetch_review_data():
    connection = get_db_connection()
    query = "SELECT customer_id, product_id, rating, sentiment_score FROM review"
    df = pd.read_sql(query, connection)
    connection.close()
    return df

def cosine_similarity(user_a, user_b, df):
    common_items = df[(df['product_id'].isin(df[df['customer_id'] == user_a]['product_id'])) &
                      (df['product_id'].isin(df[df['customer_id'] == user_b]['product_id']))]
    if common_items.empty:
        return 0
    ratings_a = common_items.groupby('product_id')['rating'].mean().values
    ratings_b = df[(df['customer_id'] == user_b) & (df['product_id'].isin(common_items['product_id']))].groupby('product_id')['rating'].mean().values
    if len(ratings_a) == 0 or len(ratings_b) == 0:
        return 0
    numerator = np.dot(ratings_a, ratings_b)
    denominator = np.linalg.norm(ratings_a) * np.linalg.norm(ratings_b)
    return numerator / denominator if denominator != 0 else 0

def generate_pivot_table(df_review):
    pivot = pd.pivot_table(df_review, index='customer_id', columns='product_id', values='rating').fillna(0)
    return pivot

def predict_ratings_with_svd(pivot, factor_n=50):
    pivot_mat = pivot.to_numpy()
    reviewer_id = list(pivot.index)
    sparse_matrix = csr_matrix(pivot_mat)
    U, sigma, Vt = svds(sparse_matrix, k=factor_n)
    sigma = np.diag(sigma)
    pred_rating = np.dot(np.dot(U, sigma), Vt)
    pred_rating_n = 1 + 4 * (pred_rating - pred_rating.min()) / (pred_rating.max() - pred_rating.min())
    pred_df = pd.DataFrame(pred_rating_n, columns=pivot.columns, index=reviewer_id)
    return pred_df

def sentiment_rating_pred(user_a, item_j, df):
    df_sentiment_equal = df[df['rating'] == df['sentiment_score']]
    items_user_a = df_sentiment_equal[(df_sentiment_equal['customer_id'] == user_a)]['product_id'].unique()
    if len(items_user_a) == 0:
        return 0
    user_item_dict = df_sentiment_equal.groupby('customer_id')['product_id'].apply(set).to_dict()
    users_with_item_j = set(df_sentiment_equal[df_sentiment_equal['product_id'] == item_j]['customer_id'])
    U = []
    items_user_a_set = set(items_user_a)
    for user_b in users_with_item_j:
        if user_b != user_a:
            items_b = user_item_dict.get(user_b, set())
            common_items = items_user_a_set & items_b
            if common_items:
                U.append(user_b) 
                break
    if len(U) > 0:
        S = []
        ra = df[df['customer_id'] == user_a]['rating'].mean()
        for user_i in U:
            sim_ai = cosine_similarity(user_a, user_i, df)
            if sim_ai > 0:
                S.append(sim_ai)
        if len(S) == 0:
            return ra

        K = 5
        top_users = np.argsort(S)[-K:]
        pr_a_j = 0
        total_sim = 0
        for index in top_users:
            user_i = U[index]
            r_i_j = df[(df['customer_id'] == user_i) & (df['product_id'] == item_j)]['rating'].mean()
            r_i_avg = df[df['customer_id'] == user_i]['rating'].mean()
            sim_ai = S[index]
            pr_a_j += sim_ai * (r_i_j - r_i_avg)
            total_sim += abs(sim_ai)
        return max(1, min(5, ra + (pr_a_j / total_sim)))
    else:
        return 0

# def sentiment_rating_pred(user_a, item_j, df):
#     items_user_a = df[(df['customer_id'] == user_a) & (df['sentiment_score'] == df['rating'])]['product_id'].unique()
#     if len(items_user_a) == 0:
#         return 0

#     user_item_dict = df.groupby('customer_id')['product_id'].apply(set).to_dict()
#     sentiment_dict = df.groupby(['customer_id', 'product_id'])[['sentiment_score', 'rating']].apply(
#         lambda x: x.values.tolist()).to_dict()

#     U = []
#     items_user_a_set = set(items_user_a)
#     for user_b, items_b in user_item_dict.items():
#         if user_b != user_a and item_j in items_b:
#             common_items = items_user_a_set & items_b
#             for item_k in common_items:
#                 sentiments_j = sentiment_dict.get((user_b, item_j), [])
#                 sentiments_k = sentiment_dict.get((user_b, item_k), [])
#                 for sentiment_j, overall_j in sentiments_j:
#                     for sentiment_k, overall_k in sentiments_k:
#                         if sentiment_j == overall_j and sentiment_k == overall_k:
#                             U.append(user_b)
#                             break
#                     if user_b in U:  
#                         break

#     if len(U) > 0:
#         S = []
#         ra = df[df['customer_id'] == user_a]['rating'].mean()
#         for user_i in U:
#             sim_ai = cosine_similarity(user_a, user_i, df)
#             if sim_ai > 0:
#                 S.append(sim_ai)
#         if len(S) == 0:
#             return ra

#         K = 5
#         top_users = np.argsort(S)[-K:]
#         pr_a_j = 0
#         total_sim = 0
#         for index in top_users:
#             user_i = U[index]
#             r_i_j = df[(df['customer_id'] == user_i) & (df['product_id'] == item_j)]['rating'].mean()
#             r_i_avg = df[df['customer_id'] == user_i]['rating'].mean()
#             sim_ai = S[index]
#             pr_a_j += sim_ai * (r_i_j - r_i_avg)
#             total_sim += abs(sim_ai)
#         return max(1, min(5, ra + (pr_a_j / total_sim)))
#     else:
#         return 0

def predict_ratings_for_user(user_id, df, pred_df):
    unrated_scores = []
    for item in df['product_id'].unique():
        if item not in df[df['customer_id'] == user_id]['product_id'].values:
            predicted_score = pred_df.at[user_id, item] if item in pred_df.columns else 0
            pr_sentiment = sentiment_rating_pred(user_id, item, df)
            pr_predicted = 0.5 * predicted_score + 0.5 * pr_sentiment
            unrated_scores.append({'customer_id': user_id, 'product_id': item, 'final_score': pr_predicted})
    
    return pd.DataFrame(unrated_scores)

@app.route('/recommendations', methods=['GET'])
def get_recommendations():
    df_review = fetch_review_data()
    user_id = request.args.get('user_id')
    if not user_id:
        return jsonify({"error": "user_id is required"}), 400
    
    rated_products = df_review[df_review['customer_id'] == user_id]['product_id'].unique()
    
    if len(rated_products) == 0:
        avg_ratings = df_review.groupby('product_id')['rating'].mean().reset_index()
        top_10_products = avg_ratings.sort_values(by='rating', ascending=False).head(10)
        top_10_product_ids = top_10_products['product_id'].tolist()
    else:
        pivot = generate_pivot_table(df_review)
        pred_df = predict_ratings_with_svd(pivot)
        predicted_df = predict_ratings_for_user(user_id, df_review, pred_df)
        top_10_df = predicted_df.sort_values(by='final_score', ascending=False).head(10)
        top_10_product_ids = top_10_df['product_id'].tolist()

    return jsonify(top_10_product_ids)
if __name__ == '__main__':
    app.run(debug=True, threaded=True)
