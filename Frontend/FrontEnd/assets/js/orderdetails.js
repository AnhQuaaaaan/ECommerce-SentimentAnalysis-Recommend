document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get("id");
    const dndk = document.querySelector(".text-dndk");
    const tk = document.querySelector(".text-tk");
    let user = JSON.parse(localStorage.getItem('user'));
    const loginLink = document.getElementById("login");
    const signupLink = document.getElementById("signup");
    if (user) {
        dndk.innerText = "Tài khoản";
        tk.innerHTML = `${user.fullname} <i class="fa-sharp fa-solid fa-caret-down"></i>`;
        loginLink.innerHTML = '<i class="fa-light fa-right-from-bracket"></i> Đăng xuất';
        loginLink.href = "#";
        signupLink.style.display = "none";
        loginLink.addEventListener("click", (event) => {
            event.preventDefault();
            localStorage.removeItem("user");
            window.location.href = "index.html";
        });
    }
    const orderId = urlParams.get("orderId");

    if (!orderId) {
        document.body.innerHTML = `<p class="error-message">Không tìm thấy đơn hàng.</p>`;
        return;
    }

    const orderInfo = document.getElementById("order-info");
    const orderItems = document.getElementById("order-items");

    const orderApi = `http://localhost:8080/api/order/${orderId}`;
    const orderItemsApi = `http://localhost:8080/api/orderItems/order/${orderId}`;

    async function fetchOrderDetails() {
        try {
            const response = await fetch(orderApi);
            if (!response.ok) throw new Error("Không thể tải chi tiết đơn hàng.");
            const order = await response.json();
            displayOrderDetails(order);
        } catch (error) {
            console.error(error);
            orderInfo.innerHTML = `<p class="error-message">Không thể tải thông tin chi tiết đơn hàng.</p>`;
        }
    }

    async function fetchOrderItems() {
        try {
            const response = await fetch(orderItemsApi);
            if (!response.ok) throw new Error("Không thể tải sản phẩm trong đơn hàng.");
            const items = await response.json();
            displayOrderItems(items);
        } catch (error) {
            console.error(error);
            orderItems.innerHTML = `<p class="error-message">Không thể tải danh sách sản phẩm.</p>`;
        }
    }

    function displayOrderDetails(order) {
        orderInfo.innerHTML = `
            <p>Mã đơn hàng: <strong>${order.id}</strong></p>
            <p>Khách hàng: <strong>${order.customerName}</strong></p>
            <p>Số điện thoại: <strong>${order.customerPhone}</strong></p>
            <p>Địa chỉ giao hàng: <strong>${order.deliveryAddress}</strong></p>
            <p>Ghi chú: <strong>${order.note || "Không có ghi chú"}</strong></p>
            <p>Ngày đặt hàng: <strong>${new Date(order.orderDate).toLocaleDateString('vi-VN')}</strong></p>
            <p>Tổng tiền: <strong>${order.totalAmount.toLocaleString()} $</strong></p>
        `;
    }

    function displayOrderItems(items) {
        if (items.length === 0) {
            orderItems.innerHTML = `<p class="no-items-message">Không có sản phẩm nào trong đơn hàng.</p>`;
            return;
        }

        orderItems.innerHTML = `
            <table class="order-items-table">
                <thead>
                    <tr>
                        <th></th>
                        <th>Tên sản phẩm</th>
                        <th>Đơn giá</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    ${items
                .map(
                    (item) => `
                                <tr>
                                    <td><img src="${item.product.image}" class="product-image" /></td>
                                    <td>${item.product.name}</td>
                                    <td>${item.product.price.toLocaleString()} $</td>
                                    <td>${item.quantity}</td>
                                    <td>${(item.quantity * item.price).toLocaleString()} $</td>
                                    <td>
                                    ${item.hasReviewed ?
                            `<span>Đã đánh giá</span>` :
                            `<button class="review-btn" onclick="showReviewForm('${item.product.id}', '${item.id}')">Đánh giá</button>`
                        }
                                </td>
                                </tr>
                            `
                )
                .join("")}
                </tbody>
            </table>
        `;
    }


    fetchOrderDetails();
    fetchOrderItems();
});
document.getElementById("back-button").addEventListener("click", () => {
    window.location.href = "orderinfo.html";
});
function viewOrderDetails(orderId) {
    window.location.href = `orderdetails.html?orderId=${orderId}`;
}
function showReviewForm(productId, orderItemId) {
    const productApi = `http://localhost:8080/api/product/${productId}`;

    fetch(productApi)
        .then(response => response.json())
        .then(product => {
            const reviewFormContainer = document.getElementById('review-form-container');
            reviewFormContainer.innerHTML = `
                    <form class="comment-form" id="review-form">
                        <div class="rating-header">
                            <p>Your Feedback</p>
                        </div>
                        <div class="product-info">
                            <img src="${product.image}" class="product-image3" />
                            <p class="product-name3">${product.name}</p>
                        </div>
                        <div class="star-rating">
                            <input type="radio" name="rate" id="rate-5-${productId}" value="5">
                            <label for="rate-5-${productId}" class="fas fa-star"></label>
                            <input type="radio" name="rate" id="rate-4-${productId}" value="4">
                            <label for="rate-4-${productId}" class="fas fa-star"></label>
                            <input type="radio" name="rate" id="rate-3-${productId}" value="3">
                            <label for="rate-3-${productId}" class="fas fa-star"></label>
                            <input type="radio" name="rate" id="rate-2-${productId}" value="2">
                            <label for="rate-2-${productId}" class="fas fa-star"></label>
                            <input type="radio" name="rate" id="rate-1-${productId}" value="1">
                            <label for="rate-1-${productId}" class="fas fa-star"></label>
                        </div>

                        <textarea placeholder="Write your comment..." required id="comment-${productId}" name="comment"></textarea>

                        <button type="submit" class="submitrv" onclick="submitReview(event, '${productId}','${orderItemId}')">Submit</button>
                    </form>
                `;
            const modal = document.getElementById('review-modal');
            modal.style.display = "flex";
        })
        .catch(error => {
            console.error("Error fetching product details:", error);
            showPopup("Error fetching product details.");
        });
}



function closeModal() {
    const modal = document.getElementById('review-modal');
    modal.style.display = "none";
}

document.querySelectorAll('.review-btn').forEach((button) => {
    button.addEventListener('click', (event) => {
        const productId = event.target.dataset.productId;
        showReviewForm(productId);
    });
});

async function submitReview(event, productId, orderItemid) {
    event.preventDefault();

    const rating = document.querySelector('input[name="rate"]:checked')?.value;
    const comment = document.getElementById(`comment-${productId}`).value;
    let user = JSON.parse(localStorage.getItem('user'));

    if (!rating || !comment) {
        showPopup("Please provide a rating and a comment.");
        return;
    }

    const reviewData = {
        rating: parseInt(rating),
        comment: comment,
        createdAt: new Date().toISOString().split('T')[0],
        customer: {
            id: user.id
        },
        product: {
            id: productId
        }
    };

    try {
        const response = await fetch("http://localhost:8080/api/review", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(reviewData)
        });

        if (!response.ok) {
            throw new Error('Failed to submit review');
        }

        const textResponse = await response.text();
        let data = {};

        if (textResponse) {
            try {
                data = JSON.parse(textResponse);
            } catch (jsonError) {
                console.error("Error parsing JSON:", jsonError);
                showPopup("Error parsing response data.");
                return;
            }
        }

        console.log("Review submitted successfully:", data);
        showPopup("Review submitted successfully!");
        await updateOrderItems(orderItemid);
        closeModal();

    } catch (error) {
        console.error("Error submitting review:", error);
        showPopup("Error submitting review.");
    }
}
function showPopup(message) {
    document.getElementById("popup-message").textContent = message;
    document.getElementById("popup").style.display = "flex";
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
    window.location.reload()
}
async function updateOrderItems(orderItemid) {
    const orderItemData = {
        id: orderItemid,
        hasReviewed: true
    };

    try {
        const response = await fetch("http://localhost:8080/api/orderItems", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(orderItemData)
        });

        if (!response.ok) {
            throw new Error('Failed to update orderItems');
        }
        console.log("OrderItem updated successfully:", updateData);

    } catch (error) {
        console.error("Error updating orderItems:", error);
    }
}