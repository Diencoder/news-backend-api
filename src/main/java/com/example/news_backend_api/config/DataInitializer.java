package com.example.news_backend_api.config;

import com.example.news_backend_api.common.SlugUtils;
import com.example.news_backend_api.model.Category;
import com.example.news_backend_api.model.News;
import com.example.news_backend_api.model.Role;
import com.example.news_backend_api.model.User;
import com.example.news_backend_api.repository.CategoryRepository;
import com.example.news_backend_api.repository.NewsRepository;
import com.example.news_backend_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final NewsRepository newsRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           NewsRepository newsRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.newsRepository = newsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            logger.info("Initializing default users...");

            User admin = new User(
                "admin",
                "admin@example.com",
                passwordEncoder.encode("password123"),
                "Administrator",
                Role.ROLE_ADMIN
            );
            userRepository.save(admin);

            User user = new User(
                "user",
                "user@example.com",
                passwordEncoder.encode("password123"),
                "Regular User",
                Role.ROLE_USER
            );
            userRepository.save(user);

            logger.info("Default users created: admin/password123, user/password123");
        }

        if (categoryRepository.count() == 0) {
            logger.info("Initializing default categories...");

            Category tech = new Category("Công nghệ", SlugUtils.toSlug("Công nghệ"), "Tin tức công nghệ mới nhất");
            Category business = new Category("Kinh doanh", SlugUtils.toSlug("Kinh doanh"), "Tin tức tài chính và kinh doanh toàn cầu");
            Category sports = new Category("Thể thao", SlugUtils.toSlug("Thể thao"), "Bản tin thể thao trong nước và quốc tế");

            categoryRepository.save(tech);
            categoryRepository.save(business);
            categoryRepository.save(sports);

            logger.info("Default categories created: Công nghệ, Kinh doanh, Thể thao");

            User admin = userRepository.findByUsername("admin").orElse(null);

            if (newsRepository.count() == 0 && admin != null) {
                logger.info("Initializing sample news articles...");

                News news1 = new News();
                news1.setTitle("Trí tuệ nhân tạo AI bùng nổ mạnh mẽ trong năm 2026");
                news1.setSlug(SlugUtils.toSlug(news1.getTitle()));
                news1.setSummary("Các mô hình AI tiên tiến đang thay đổi cách lập trình và phát triển phần mềm toàn cầu.");
                news1.setContent("Năm 2026 đánh dấu bước tiến vượt bậc của trí tuệ nhân tạo (AI) trong lĩnh vực kỹ thuật phần mềm. Các trợ lý AI không chỉ hỗ trợ viết mã mà còn tham gia vào thiết kế hệ thống, tối ưu hiệu năng và kiểm thử tự động.");
                news1.setThumbnailUrl("https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe");
                news1.setCategory(tech);
                news1.setAuthor(admin);
                news1.setPublished(true);
                news1.setViews(1250L);
                newsRepository.save(news1);

                News news2 = new News();
                news2.setTitle("Thị trường công nghệ tài chính FinTech tăng trưởng kỷ lục");
                news2.setSlug(SlugUtils.toSlug(news2.getTitle()));
                news2.setSummary("Thanh toán số và ngân hàng mở thúc đẩy các công ty FinTech đạt mức vốn hóa mới.");
                news2.setContent("Sự bùng nổ của thanh toán không dùng tiền mặt và các giải pháp ngân hàng tích hợp đã giúp ngành FinTech tăng trưởng 35% so với cùng kỳ năm trước.");
                news2.setThumbnailUrl("https://images.unsplash.com/photo-1559526324-4b87b5e36e44");
                news2.setCategory(business);
                news2.setAuthor(admin);
                news2.setPublished(true);
                news2.setViews(820L);
                newsRepository.save(news2);

                News news3 = new News();
                news3.setTitle("Đội tuyển Việt Nam chuẩn bị cho giải đấu vòng loại World Cup");
                news3.setSlug(SlugUtils.toSlug(news3.getTitle()));
                news3.setSummary("Ban huấn luyện công bố danh sách tập trung gồm nhiều cầu thủ trẻ triển vọng.");
                news3.setContent("Đội tuyển quốc gia Việt Nam đã chính thức hội quân để chuẩn bị cho chuỗi trận đấu quan trọng sắp tới với quyết tâm giành tấm vé đi tiếp.");
                news3.setThumbnailUrl("https://images.unsplash.com/photo-1508098682722-e99c43a406b2");
                news3.setCategory(sports);
                news3.setAuthor(admin);
                news3.setPublished(true);
                news3.setViews(2430L);
                newsRepository.save(news3);

                logger.info("Sample news initialized successfully.");
            }
        }
    }
}
