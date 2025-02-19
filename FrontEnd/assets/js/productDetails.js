let sp;
let token = localStorage.getItem("token");
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
    const fetchProductDetails = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/product/${id}`);
            if (!response.ok) {
                throw new Error("Unable to fetch product details");
            }
            const product = await response.json();
            sp = product
            renderProductDetails(product);
        } catch (error) {
            console.error("Error:", error.message);
        }
    };

    const cleanDescription = (description) => {
        const images = [];
        description = description.replace(/<img[^>]*src="([^"]*)"[^>]*>/gi, (match, src) => {
            images.push(`<img src="${src}" class="product-image-inline">`);
            return '';
        });
        description = description.replace(/<div[^>]*>[\s\S]*?<\/div>/gi, '');
        description = description.replace(/^[,\'"\s]+/, '');
        let cleanedDescription = description
            .replace(/', '/g, '')
            .replace(/['"]/g, '')
            .replace(/<br\s*\/?>/g, '<br>')
            .replace(/\\n+/g, '')
            .replace(/\n+/g, '')
            .replace(/\s+/g, ' ')
            .replace(/^\s+|\s+$/g, '')
            .trim();

        if (images.length > 0) {
            cleanedDescription = cleanedDescription + '<br><strong>Hình ảnh tham khảo:</strong><br>' + images.join('');
        }
        return cleanedDescription;
    };
    const replaceCommasWithNewlines = (text) => {
        let flag = false; // Biến để kiểm tra sự xuất hiện của ":"

        // Duyệt qua từng ký tự của chuỗi
        return text.replace(/(.)/g, (match) => {
            if (match === ':') {
                flag = true; // Nếu gặp dấu ":", gán flag = true
                return match; // Giữ nguyên dấu ":"
            }

            if (match === ',') {
                // Nếu gặp dấu "," và flag là true, thì thay thế dấu "," bằng "\n"
                if (flag) {
                    flag = false; // Sau khi thay dấu "," bằng "\n", reset lại flag
                    return '\n'; // Thay dấu "," bằng dấu xuống dòng
                }
                return match; // Nếu flag là false, giữ nguyên dấu ","
            }

            return match; // Giữ nguyên tất cả các ký tự khác
        });
    };
    const cleanText = (text) => {
        // Loại bỏ tất cả ký tự \n và khoảng trắng thừa
        return text
            .replace(/\\n+/g, '')
            .replace(/\n+/g, '')
            .replace(/\s+/g, ' ')
            .replace(/^\s+|\s+$/g, '')
            .replace(/([a-zA-Z]):/g, '$1') // Loại bỏ dấu ':' nếu đứng ngay sau chữ cái (a-zA-Z)
            .replace(/[{}]/g, '') // Loại bỏ các dấu { và }
            .replace(/,\s*'/g, '<br>')  // Thay dấu phẩy sau dấu nháy đơn
            .replace(/\(/g, '')  // Loại bỏ dấu ngoặc đơn mở '('
            .replace(/\)/g, '')  // Loại bỏ dấu ngoặc đơn đóng ')'
            .trim();
    };
    const cleanText1 = (text) => {
        return text
            .replace(/'/g, '')  // Loại bỏ tất cả dấu nháy đơn
            .trim();
    };
    const renderProductDetails = (product) => {
        let imginfo = document.getElementById("imginfo");
        let nameinfo = document.getElementById("nameinfo");
        let priceinfo = document.getElementById("priceinfo");
        let desinfo = document.getElementById("desinfo");
        let details = document.getElementById("detailsinfo");
        const cleanedDescription = cleanDescription(product.description);
        imginfo.src = product.image;
        nameinfo.textContent = product.name;
        priceinfo.innerHTML = `<span>$  ${product.price}</span>`;
        desinfo.innerHTML = cleanedDescription;
        const formattedDetails = cleanText1(cleanText(product.details));
        details.innerHTML = formattedDetails || 'No additional details available';
    };

    if (productId) {
        fetchProductDetails(productId);
    } else {
        console.error("Product ID not found in URL");
    }

    const fetchReviewDetails = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/review/${id}`);
            if (!response.ok) {
                throw new Error("Unable to fetch product reviews");
            }
            const reviews = await response.json();
            renderReviewDetails(reviews);
        } catch (error) {
            console.error("Error:", error.message);
        }
    };

    const renderReviewDetails = (reviews) => {
        const reviewSection = document.querySelector(".rating-section .comments");
        reviewSection.innerHTML = "";

        if (reviews && reviews.length > 0) {
            let totalRating = 0;

            reviews.forEach(review => {
                const commentDiv = document.createElement("div");
                commentDiv.className = "comment";
                const starIcons = '★'.repeat(review.rating) + '☆'.repeat(5 - review.rating);

                const formattedDate = new Date(review.createdAt).toLocaleDateString("vi-VN", {
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                });

                commentDiv.innerHTML = `
                    <div class="comment-header">
                        <strong>${review.customer.fullname}</strong>
                        <span class="rating">${starIcons}</span>
                    </div>
                    <p class="comment-text">${review.comment}</p>
                    <div class="comment-date">Đánh giá vào: ${formattedDate}</div>
                `;
                reviewSection.appendChild(commentDiv);

                totalRating += review.rating;
            });

            let averageRating = totalRating / reviews.length;
            let roundedRating = parseFloat(averageRating.toFixed(1));
            let fullStars = Math.floor(roundedRating);
            let halfStars = (roundedRating % 1) >= 0.5 ? 1 : 0;
            let emptyStars = 5 - fullStars - halfStars;

            let stars = generateStars(fullStars, halfStars, emptyStars);

            const ratingSection = document.querySelector(".product-rating1 .stars");
            const ratingText = document.querySelector(".product-rating1 span");

            ratingText.textContent = `${averageRating.toFixed(1)}`;
            ratingSection.innerHTML = stars;
        } else {
            reviewSection.innerHTML = "<p class='no-reviews'>No reviews available for this product.</p>";
        }
    };
    const generateStars = (fullStars, halfStars = 0, emptyStars = 0) => {
        let starHTML = '';
        for (let i = 0; i < fullStars; i++) {
            starHTML += '<i class="fas fa-star"></i>';
        }
        for (let i = 0; i < halfStars; i++) {
            starHTML += '<i class="fas fa-star-half-alt"></i>';
        }
        for (let i = 0; i < emptyStars; i++) {
            starHTML += '<i class="far fa-star"></i>';
        }
        return starHTML;
    };

    if (productId) {
        fetchReviewDetails(productId);
    } else {
        console.error("Product ID not found in URL");
    }
});

async function submitReview(event) {
    event.preventDefault();

    const rating = document.querySelector('input[name="rate"]:checked')?.value;
    const comment = document.getElementById("comment").value;
    let user = JSON.parse(localStorage.getItem('user'));
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get("id");

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
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
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
// function addToCart() {
//     let cart = JSON.parse(localStorage.getItem('cart')) || [];
//     const existingProductIndex = cart.findIndex(item => item.id === sp.id);
//     if (existingProductIndex !== -1) {
//         cart[existingProductIndex].quantity += 1;
//     } else {
//         sp.quantity = 1;
//         cart.push(sp);
//     }
//     localStorage.setItem('cart', JSON.stringify(cart));
//     updateCartCount()
//     animationCart()
// }
async function addToCart() {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    if (!userId) {
        console.error('User ID không tồn tại trong localStorage.');
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get("id");
    if (!productId) {
        console.error('Product ID không tồn tại trong URL.');
        return;
    }

    const requestBody = {
        customer: { id: userId }
    };

    try {
        const response = await fetch(`http://localhost:8080/api/cart/${productId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(requestBody),
        });
        if (response.ok) {
            updateCartCount();
            animationCart();
        } else {
            console.error('Thêm sản phẩm vào giỏ hàng thất bại:', await response.text());
        }
    } catch (error) {
        console.error('Lỗi khi gọi API:', error);
    }
}
async function updateTotalPrice() {
    try {
        const response = await fetch('http://localhost:8080/api/cartItems/customer/A9MHZQO27LL00V');
        if (!response.ok) {
            console.error('Lỗi khi lấy dữ liệu giỏ hàng:', await response.text());
            return;
        }

        const cart = await response.json();
        const totalPrice = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

        const roundedTotalPrice = totalPrice.toFixed(2);

        const totalPriceElement = document.querySelector('.cart-total-price .text-price');
        if (totalPriceElement) {
            totalPriceElement.textContent = roundedTotalPrice + '$';
        }
        updateCartCount();
    } catch (error) {
        console.error('Lỗi khi cập nhật tổng giá:', error);
    }
}
function animationCart() {
    const countElement = document.querySelector(".count-product-cart");
    if (countElement) {
        countElement.style.animation = "slidein ease 0.5s";
        setTimeout(() => {
            countElement.style.animation = "none";
        }, 500);
    }
}

