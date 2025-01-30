document.addEventListener("DOMContentLoaded", () => {
    updateCartCount();
    const apiEndpoint = "http://localhost:8080/api/product";
    const pageNavList = document.querySelector(".page-nav-list");
    const homeProducts = document.getElementById("home-products");
    let currentPage = 0;
    let totalPages = 0;
    const pageSize = 20;
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
            window.location.reload();
        });
    }
    const fetchProducts = async (page = 0) => {
        try {
            const response = await fetch(`${apiEndpoint}?page=${page}&size=${pageSize}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            totalPages = data.totalPages;
            renderProducts(data.content);
            renderPagination();
        } catch (error) {
            console.error("Có lỗi xảy ra khi gọi API:", error);
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
                        <img class="product-image" src="${product.image}" >
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

    const renderPagination = () => {
        pageNavList.innerHTML = "";

        const prevButton = document.createElement("li");
        prevButton.className = "page-item";
        prevButton.innerHTML = `
            <button class="page-button" data-page="${currentPage - 1}" ${currentPage === 0 ? "disabled" : ""}> &lt; </button>
        `;
        pageNavList.appendChild(prevButton);

        for (let i = 0; i < totalPages; i++) {
            const pageItem = document.createElement("li");
            pageItem.className = "page-item";

            const pageButton = document.createElement("button");
            pageButton.className = "page-button";
            pageButton.dataset.page = i;
            pageButton.textContent = i + 1;

            if (i === currentPage) {
                pageItem.classList.add("active");
            }

            pageItem.appendChild(pageButton);
            pageNavList.appendChild(pageItem);
        }

        const nextButton = document.createElement("li");
        nextButton.className = "page-item";
        nextButton.innerHTML = `
            <button class="page-button" data-page="${currentPage + 1}" ${currentPage === totalPages - 1 ? "disabled" : ""}> &gt; </button>
        `;
        pageNavList.appendChild(nextButton);

        addPaginationEventListeners();
    };

    const addPaginationEventListeners = () => {
        const pageButtons = document.querySelectorAll(".page-button");
        pageButtons.forEach(button => {
            button.addEventListener("click", (event) => {
                const page = parseInt(event.target.dataset.page);
                if (page >= 0 && page < totalPages) {
                    currentPage = page;
                    fetchProducts(page);
                }
            });
        });
    };

    fetchProducts(currentPage);
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