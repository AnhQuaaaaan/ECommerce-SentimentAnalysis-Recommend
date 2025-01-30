document.addEventListener("DOMContentLoaded", () => {
    const dndk = document.querySelector(".text-dndk");
    const tk = document.querySelector(".text-tk");
    let user = JSON.parse(localStorage.getItem("user"));
    const loginLink = document.getElementById("login");
    const signupLink = document.getElementById("signup");
    const loadingPopup = document.getElementById("loadingPopup");
    const homeProducts = document.getElementById("home-products");

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

    let id = user.id;
    const apiUrl = `http://localhost:8080/api/product/recommendation/${id}`;

    const fetchProducts = async () => {
        try {
            loadingPopup.style.display = "flex";

            const response = await fetch(`${apiUrl}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            renderProducts(data);
        } catch (error) {
            console.error("Có lỗi xảy ra khi gọi API:", error);
        } finally {
            loadingPopup.style.display = "none";
        }
    };

    const renderProducts = (products) => {
        homeProducts.innerHTML = "";
        products.forEach((product) => {
            const listItem = document.createElement("li");
            listItem.className = "product-item";
            listItem.innerHTML = `
                <div class="product-card">
                    <div class="product-image-container">
                        <img class="product-image" src="${product.image}">
                    </div>
                    <div class="product-info">
                        <h3 class="product-name">${product.name}</h3>
                        <p class="product-price">${product.price} $</p>
                        <button class="details-button" onclick="productdetails('${product.id}')">
                            <i class="fas fa-eye"></i>
                            Xem chi tiết
                        </button>
                    </div>
                </div>
            `;
            homeProducts.appendChild(listItem);
        });
    };

    fetchProducts();
    updateCartCount();
});

function productdetails(productId) {
    window.location.href = `productDetails.html?id=${productId}`;
}
function updateCartCount() {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    const uniqueProductIds = new Set(cart.map(item => item.id));
    const uniqueProductCount = uniqueProductIds.size;
    document.querySelector('.count-product-cart').textContent = uniqueProductCount;
}