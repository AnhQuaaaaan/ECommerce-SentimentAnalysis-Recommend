document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    let currentPage = parseInt(urlParams.get("page")) || 0;
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

    const apiUrl = `http://localhost:8080/api/order/customer/${user.id}?page=${currentPage}&size=12`;
    const homeProducts = document.getElementById("home-products");
    const paginationContainer = document.getElementById("pagination");

    async function fetchOrders() {
        try {
            const response = await fetch(apiUrl);
            if (!response.ok) {
                throw new Error("Failed to fetch orders");
            }
            const orders = await response.json();
            displayOrders(orders.content);
            displayPagination(orders.totalPages);
        } catch (error) {
            console.error("Error fetching orders:", error);
            homeProducts.innerHTML = `<p class="error-message">Không thể tải danh sách đơn hàng.</p>`;
        }
    }

    function displayOrders(orders) {
        if (orders.length === 0) {
            homeProducts.innerHTML = `<p class="no-orders-message">Bạn chưa có đơn hàng nào.</p>`;
            return;
        }

        homeProducts.innerHTML = orders
            .map(order => `
                <div class="order-card">
                    <h3>Mã đơn hàng: ${order.id}</h3>
                    <p>Ngày đặt hàng: ${new Date(order.orderDate).toLocaleDateString()}</p>
                    <p>Tổng tiền: ${order.totalAmount.toLocaleString()} $</p>
                    <p class="order-status">Ghi chú: ${order.note || "Không có ghi chú"}</p>
                    <button class="view-details-btn" onclick="viewOrderDetails('${order.id}')">Xem chi tiết</button>
                </div>
            `)
            .join("");
    }

    function displayPagination(totalPages) {
        paginationContainer.innerHTML = "";

        const prevButton = document.createElement("button");
        prevButton.innerText = "<";
        prevButton.disabled = currentPage === 0;
        prevButton.addEventListener("click", () => {
            if (currentPage > 0) {
                currentPage--;
                updatePage();
            }
        });
        prevButton.style.backgroundColor = "var(--red)";
        paginationContainer.appendChild(prevButton);

        for (let i = 0; i < totalPages; i++) {
            const pageButton = document.createElement("button");
            pageButton.innerText = i + 1;
            pageButton.addEventListener("click", () => {
                currentPage = i;
                updatePage();
            });
            pageButton.style.backgroundColor = "var(--red)";
            if (i === currentPage) {
                pageButton.style.backgroundColor = "#007bff";
            }
            paginationContainer.appendChild(pageButton);
        }

        const nextButton = document.createElement("button");
        nextButton.innerText = ">";
        nextButton.disabled = currentPage === totalPages - 1;
        nextButton.addEventListener("click", () => {
            if (currentPage < totalPages - 1) {
                currentPage++;
                updatePage();
            }
        });
        nextButton.style.backgroundColor = "var(--red)";
        paginationContainer.appendChild(nextButton);
    }

    function updatePage() {
        const newUrl = `?page=${currentPage}`;
        window.location.search = newUrl;
        fetchOrders();
    }

    fetchOrders();
});

function viewOrderDetails(orderId) {
    window.location.href = `orderdetails.html?orderId=${orderId}`;
}
