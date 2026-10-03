# 📰 News Backend API - Hệ Thống Quản Lý Tin Tức (Nhiệm Vụ 17)

Dự án Backend API quản lý và đọc tin tức được xây dựng bằng **Java** & **Spring Boot**, tích hợp xác thực bảo mật **JWT (JSON Web Token)**, phân quyền **RBAC (Role-Based Access Control)**, cơ sở dữ liệu **JPA / Hibernate** (hỗ trợ **H2** chạy tức thì & **MySQL**), chuẩn hóa **Request/Response** và hỗ trợ **Upload / Quản lý tệp tin**.

🔗 **GitHub Repository:** [http://github.com/Diencoder/news-backend-api](http://github.com/Diencoder/news-backend-api)

---

## 📋 Danh Sách Yêu Cầu Hoàn Thành (Checklist)

- [x] **Chọn 1 ngôn ngữ làm Backend:** Java (Spring Boot)
- [x] **Tìm hiểu và xây dựng cấu trúc dự án:** Phân tầng chuẩn Controller - Service - Repository - Model - DTO - Security - Exception
- [x] **Xây dựng biến môi trường, biến toàn cục:** 
  - Biến môi trường qua `.env.example` và `application.properties`
  - Biến toàn cục / hằng số hệ thống qua `AppConstants.java`
- [x] **Xây dựng cấu trúc Request / Response chuẩn:** 
  - `ApiResponse<T>` chuẩn hóa mọi phản hồi thành công và thất bại
  - `PagedResponse<T>` chuẩn hóa kết quả phân trang
  - `GlobalExceptionHandler` bắt lỗi tự động (`@RestControllerAdvice`)
- [x] **Tạo API Đăng ký / Đăng nhập:**
  - `POST /api/auth/register` (Validation đầy đủ)
  - `POST /api/auth/login` (Xác thực JWT token)
  - `GET /api/auth/me` (Lấy thông tin tài khoản hiện tại)
- [x] **Tạo API Public (Đọc tin tức):**
  - `GET /api/public/news` (Danh sách bài viết, phân trang, lọc danh mục, tìm kiếm)
  - `GET /api/public/news/{id}` (Chi tiết theo ID, tự động tăng lượt xem `views`)
  - `GET /api/public/news/slug/{slug}` (Chi tiết theo đường dẫn thân thiện SEO)
  - `GET /api/public/news/top` (Top 5 bài viết nhiều lượt xem nhất)
  - `GET /api/public/news/latest` (Top 5 bài viết mới nhất)
  - `GET /api/public/categories` (Danh sách danh mục tin tức)
- [x] **Tạo API Private (Thêm, Xóa, Sửa, Tìm kiếm, Sắp xếp):**
  - `POST /api/admin/news` (Thêm tin tức, tự động gán tác giả)
  - `PUT /api/admin/news/{id}` (Chỉnh sửa tin tức)
  - `DELETE /api/admin/news/{id}` (Xóa tin tức)
  - `GET /api/admin/news` (Tìm kiếm theo từ khóa, lọc theo danh mục, lọc trạng thái xuất bản, sắp xếp linh hoạt theo mọi trường `asc`/`desc`, phân trang)
- [x] **Tạo API Upload / Xóa file:**
  - `POST /api/files/upload` (Upload file, kiểm tra định dạng và dung lượng, tạo tên file chống trùng lặp UUID)
  - `DELETE /api/files/{fileName}` (Xóa file trên server)
  - `GET /uploads/{fileName}` & `GET /api/files/download/{fileName}` (Truy cập trực tiếp file tĩnh hoặc tải về)

---

## 🏛️ Cấu Trúc Dự Án (Project Structure)

```
news-backend-api/
├── .mvn/wrapper/                  # Maven wrapper
├── src/
│   ├── main/
│   │   ├── java/com/example/news_backend_api/
│   │   │   ├── common/            # Cấu trúc chung & Hằng số toàn cục
│   │   │   │   ├── ApiResponse.java       # Định dạng Response đồng nhất
│   │   │   │   ├── AppConstants.java      # Biến toàn cục (Pagination, File size, Roles)
│   │   │   │   ├── PagedResponse.java     # Cấu trúc Response phân trang
│   │   │   │   └── SlugUtils.java         # Tạo SEO slug chuẩn hóa tiếng Việt
│   │   │   ├── config/            # Cấu hình hệ thống & Khởi tạo dữ liệu
│   │   │   │   ├── DataInitializer.java   # Tự động nạp dữ liệu mẫu khi khởi động
│   │   │   │   ├── SecurityConfig.java    # Spring Security, CORS, JWT Filter
│   │   │   │   └── WebMvcConfig.java      # Cấu hình phục vụ file tĩnh /uploads/**
│   │   │   ├── controller/        # Tầng Controller (REST APIs)
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── PublicNewsController.java
│   │   │   │   ├── AdminNewsController.java
│   │   │   │   ├── AdminCategoryController.java
│   │   │   │   └── FileController.java
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── request/       # DTO nhận từ Client kèm Validation
│   │   │   │   │   ├── CategoryRequest.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── NewsRequest.java
│   │   │   │   │   └── RegisterRequest.java
│   │   │   │   └── response/      # DTO trả về cho Client
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── CategoryResponse.java
│   │   │   │       ├── FileUploadResponse.java
│   │   │   │       ├── NewsResponse.java
│   │   │   │       └── UserResponse.java
│   │   │   ├── exception/         # Xử lý ngoại lệ toàn cục
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── FileStorageException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   ├── model/             # Thực thể JPA (Entities)
│   │   │   │   ├── Category.java
│   │   │   │   ├── News.java
│   │   │   │   ├── Role.java
│   │   │   │   └── User.java
│   │   │   ├── repository/        # Tầng thao tác CSDL (Spring Data JPA)
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── NewsRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/          # Bảo mật JWT
│   │   │   │   ├── jwt/
│   │   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   └── JwtUtils.java
│   │   │   │   ├── UserDetailsImpl.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   └── service/           # Tầng nghiệp vụ (Business Logic)
│   │   │       ├── AuthService.java
│   │   │       ├── CategoryService.java
│   │   │       ├── FileStorageService.java
│   │   │       ├── NewsService.java
│   │   │       └── impl/          # Hiện thực Service
│   │   └── resources/
│   │       └── application.properties # Cấu hình ứng dụng
│   └── test/                      # Kiểm thử tự động
├── .env.example                   # Mẫu khai báo biến môi trường
├── .gitignore
├── mvnw / mvnw.cmd
├── pom.xml
└── README.md
```

---

## ⚙️ Cấu Hình Biến Môi Trường & Toàn Cục

### 1. Biến Môi Trường (`.env.example` / `application.properties`)

Hệ thống mặc định cấu hình **H2 Database file persistent** (`./data/newsdb`) giúp dự án có thể chạy ngay lập tức mà **không cần cài đặt MySQL trước**.

Khi muốn kết nối **MySQL**, chỉ cần cấu hình trong môi trường hoặc sửa `application.properties`:

```properties
# Server
SERVER_PORT=8080

# Cấu hình CSDL MySQL (Nếu dùng MySQL)
DB_URL=jdbc:mysql://localhost:3306/news_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_password
DB_DRIVER=com.mysql.cj.jdbc.Driver
JPA_DDL_AUTO=update

# Bảo mật JWT
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION_MS=86400000

# Quản lý Upload File
FILE_UPLOAD_DIR=./uploads
MAX_FILE_SIZE=10MB
MAX_REQUEST_SIZE=15MB
```

### 2. Biến Toàn Cục (`AppConstants.java`)

```java
public static final String DEFAULT_PAGE_NUMBER = "0";
public static final String DEFAULT_PAGE_SIZE = "10";
public static final String DEFAULT_SORT_BY = "createdAt";
public static final String DEFAULT_SORT_DIRECTION = "desc";
public static final String[] ALLOWED_FILE_EXTENSIONS = {
    ".jpg", ".jpeg", ".png", ".webp", ".gif", ".pdf", ".docx", ".txt"
};
public static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
```

---

## 📦 Định Dạng Request / Response Chuẩn

Mọi API phản hồi theo cùng một cấu trúc JSON thống nhất:

### Thành công:
```json
{
  "success": true,
  "status": 200,
  "message": "Operation successful",
  "data": { ... },
  "errors": null,
  "timestamp": "2026-10-03T09:49:51.5710918"
}
```

### Thất bại / Lỗi Validation (400 Bad Request):
```json
{
  "success": false,
  "status": 400,
  "message": "Validation failed",
  "data": null,
  "errors": {
    "username": "Username cannot be blank",
    "password": "Password must be at least 6 characters"
  },
  "timestamp": "2026-10-03T09:50:49.7751718"
}
```

---

## 👤 Dữ Liệu Khởi Tạo Tự Động (Data Seeder)

Khi khởi động ứng dụng lần đầu, `DataInitializer` sẽ tự động tạo dữ liệu mẫu:

| Loại | Tài khoản / Dữ liệu | Mật khẩu / Chi tiết | Vai trò |
| :--- | :--- | :--- | :--- |
| **Admin Account** | `admin` (hoặc `admin@example.com`) | `password123` | `ROLE_ADMIN` |
| **User Account** | `user` (hoặc `user@example.com`) | `password123` | `ROLE_USER` |
| **Danh mục** | `Công nghệ`, `Kinh doanh`, `Thể thao` | - | - |
| **Tin tức mẫu** | 3 bài viết kèm nội dung, tác giả, lượt xem, hình ảnh minh họa | - | - |

---

## 🚀 Hướng Dẫn Chạy Dự Án

### Yêu cầu:
- Java JDK 21 trở lên (Đã kiểm thử hoạt động tương thích hoàn toàn trên OpenJDK 27).

### Khởi động Server:
```powershell
# Trên Windows PowerShell:
.\mvnw.cmd spring-boot:run
```

Server sẽ khởi động tại: `http://localhost:8080`

---

## 📖 Chi Tiết Danh Sách API & Ví Dụ cURL

### 1. Nhóm API Xác Thực (Authentication)

#### 📝 Đăng ký tài khoản:
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/register`
- **Body:**
```json
{
  "username": "sinhvien01",
  "email": "sinhvien01@example.com",
  "password": "password123",
  "fullName": "Nguyen Van A"
}
```

#### 🔑 Đăng nhập:
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/login`
- **Body:**
```json
{
  "usernameOrEmail": "admin",
  "password": "password123"
}
```
*Phản hồi sẽ bao gồm Bearer JWT Token để sử dụng cho các API Private.*

#### 👤 Lấy thông tin tài khoản hiện tại:
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/auth/me`
- **Header:** `Authorization: Bearer <TOKEN>`

---

### 2. Nhóm API Public (Đọc tin tức - Không cần đăng nhập)

#### 📰 Đọc danh sách tin tức (Kèm Phân trang, Lọc, Tìm kiếm):
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/public/news?page=0&size=10&sortBy=createdAt&sortDir=desc`
- **Query params tùy chọn:**
  - `page`: Số trang (mặc định: `0`)
  - `size`: Kích thước trang (mặc định: `10`)
  - `sortBy`: Sắp xếp theo trường (`createdAt`, `views`, `title`, ...)
  - `sortDir`: Hướng sắp xếp (`asc` hoặc `desc`)
  - `categoryId`: Lọc theo ID danh mục
  - `keyword`: Tìm kiếm bài viết theo từ khóa

#### 🔍 Chi tiết bài viết theo ID (Tự động +1 view):
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/public/news/1`

#### 🌐 Chi tiết bài viết theo Slug SEO:
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/public/news/slug/tri-tue-nhan-tao-ai-bung-no-manh-me-trong-nam-2026`

#### 🔥 Top 5 bài viết nhiều lượt xem nhất:
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/public/news/top`

#### 🕒 Top 5 bài viết mới nhất:
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/public/news/latest`

#### 📂 Danh sách danh mục:
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/public/categories`

---

### 3. Nhóm API Private (Thêm, Xóa, Sửa, Tìm kiếm, Sắp xếp Tin Tức)
> *Yêu cầu Header:* `Authorization: Bearer <TOKEN>`

#### ✍️ Thêm tin tức mới:
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/admin/news`
- **Header:** `Authorization: Bearer <TOKEN>`
- **Body:**
```json
{
  "title": "Ra mắt thế hệ xe điện thông minh mới",
  "summary": "Mẫu xe mới ứng dụng pin thể rắn tăng 50% quãng đường di chuyển.",
  "content": "Nội dung chi tiết về các thông số kỹ thuật và công nghệ đột phá của mẫu xe điện...",
  "thumbnailUrl": "https://example.com/images/car.jpg",
  "categoryId": 1,
  "published": true
}
```

#### ✏️ Cập nhật tin tức:
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/admin/news/{id}`
- **Header:** `Authorization: Bearer <TOKEN>`

#### 🗑️ Xóa tin tức:
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/admin/news/{id}`
- **Header:** `Authorization: Bearer <TOKEN>`

#### 🔎 Quản lý & Tìm kiếm bài viết (Hỗ trợ tìm kiếm, lọc bản nháp, sắp xếp):
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/admin/news?keyword=AI&categoryId=1&published=true&sortBy=views&sortDir=desc`
- **Header:** `Authorization: Bearer <TOKEN>`

---

### 4. Nhóm API Upload & Xóa File
> *Yêu cầu Header:* `Authorization: Bearer <TOKEN>`

#### 📤 Upload file:
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/files/upload`
- **Header:** `Authorization: Bearer <TOKEN>`
- **Body:** `form-data` với key là `file`
```bash
curl -X POST "http://localhost:8080/api/files/upload" \
  -H "Authorization: Bearer <TOKEN>" \
  -F "file=@/duong/dan/anh.jpg"
```
*Phản hồi trả về `fileDownloadUri` (ví dụ: `http://localhost:8080/uploads/uuid_anh.jpg`).*

#### 🗑️ Xóa file:
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/files/{fileName}`
- **Header:** `Authorization: Bearer <TOKEN>`

#### 👁️ Xem trực tiếp file:
- Truy cập trình duyệt: `http://localhost:8080/uploads/{fileName}`
- Hoặc API tải về: `GET http://localhost:8080/api/files/download/{fileName}`

---

## 📌 Hướng Dẫn Đẩy Code Lên GitHub

Kho lưu trữ GitHub đã được liên kết với dự án:
`https://github.com/Diencoder/news-backend-api`

Thực hiện đẩy toàn bộ bài làm lên GitHub bằng các lệnh sau:

```bash
git add .
git commit -m "feat: complete mission 17 with full news api, auth jwt, public/private endpoints, and file upload"
git push origin main
```
