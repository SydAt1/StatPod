<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us | StatPod</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/aboutus.css">
</head>
<body>
    <%@ include file="header.jsp" %>
    <section class="hero">
        <h1>About Us</h1>
        <p>We are you one stop solution for all your podcast needs</p>
        
        <img src="${pageContext.request.contextPath}/images/about-us/mascots/mascot.png" alt="Floating egg" class="floating-egg">
    </section>
    
    <section class="timeline">
        <div class="timeline-item">
            <div class="timeline-content">
                <h3 class="timeline-date">DECEMBER 2024</h3>
                <p class="timeline-text">Start of StatPod's first application on Java Platform</p>
            </div>
            <div class="timeline-dot"></div>
        </div>
        
        <div class="timeline-item">
            <div class="timeline-content">
                <h3 class="timeline-date">JANUARY 2025</h3>
                <p class="timeline-text">Release of the StatPod's first Application</p>
            </div>
            <div class="timeline-dot"></div>
        </div>
        
        <div class="timeline-item">
            <div class="timeline-content">
                <h3 class="timeline-date">FEBRUARY 2025</h3>
                <p class="timeline-text">Successful completion of StatPod's application on Java Platform.</p>
            </div>
            <div class="timeline-dot"></div>
        </div>
        
        <div class="timeline-item">
            <div class="timeline-content">
                <h3 class="timeline-date">March 2025</h3>
                <p class="timeline-text">After recognizing the need for top notch podcast streaming, start of StatPod Web</p>
            </div>
            <div class="timeline-dot"></div>
        </div>
        
        <div class="timeline-item">
            <div class="timeline-content">
                <h3 class="timeline-date">April 2025</h3>
                <p class="timeline-text">All the requirements and interesting things that made StatPod great are baked into StatPod web.</p>
            </div>
            <div class="timeline-dot"></div>
        </div>
        
        <div class="timeline-item">
            <div class="timeline-content">
                <h3 class="timeline-date">MAY 2025</h3>
                <p class="timeline-text">First beta release of the all new StatPod Web.</p>
            </div>
            <div class="timeline-dot"></div>
        </div>
        
        <div class="timeline-item">
            <div class="timeline-content">
                <h3 class="timeline-date">JUNE 2025</h3>
                <p class="timeline-text">Successful release of all complete StatPod web.</p>
            </div>
            <div class="timeline-dot"></div>
        </div>
    </section>
    
    <section class="features">
        <h2>What Makes Us Special</h2>
        
        <div class="features-grid">
            <div class="feature-card">
                <img src="${pageContext.request.contextPath}/images/about-us/headphone-icon.svg" alt="Headphone Icon" class="feature-icon">
                <h3 class="feature-title">Smart Podcast Player</h3>
                <p class="feature-description">Seamless listening with automatic progress saving and cross-device sync.</p>
            </div>
            
            <div class="feature-card">
                <img src="${pageContext.request.contextPath}/images/about-us/library-icon.svg" alt="Library Icon" class="feature-icon">
                <h3 class="feature-title">Organized Library</h3>
                <p class="feature-description">Beautifully curated episodes sorted by date, topic, and duration for easy discovery.</p>
            </div>
            
            <div class="feature-card">
                <img src="${pageContext.request.contextPath}/images/about-us/playlist-icon.svg"  alt="Playlist Icon" class="feature-icon">
                <h3 class="feature-title">Personalized Tracker</h3>
                <p class="feature-description">Never lose your place – we remember where you left off and mark listened episodes.</p>
            </div>
            
            <div class="feature-card">
                <img src="${pageContext.request.contextPath}/images/about-us/search_icon.svg" alt="Search Icon" class="feature-icon">
                <h3 class="feature-title">Lightning Search</h3>
                <p class="feature-description">Find any episode instantly with smart keyword search and filters.</p>
            </div>
            
            <div class="feature-card">
                <img src="${pageContext.request.contextPath}/images/about-us/heart-icon.svg"  class="feature-icon">
                <h3 class="feature-title">Favorites Hub</h3>
                <p class="feature-description">Save beloved episodes in one click.</p>
            </div>
            
            <div class="feature-card">
                <img src="${pageContext.request.contextPath}/images/about-us/dashboard-icon.svg" alt="Nitro Icon" class="feature-icon">
                <h3 class="feature-title">Creator Dashboard</h3>
                <p class="feature-description">Easy uploads, analytics, and management tools for podcasters.</p>
            </div>
        </div>
    </section>
    
    <section class="community">
        <h2>Join Our Growing Community</h2>
        <p>Over 200 million people use our platform to talk, hang out, and create together. Whether you're part of a school club, gaming group, worldwide art community, or just a handful of friends that want to spend time together, we make it easy to talk every day and hang out more often.</p>
        
        <a href="#" class="cta-button">Join Now</a>

    </section>
    <%@ include file="footer.jsp" %>
    
        <script>
        // Scroll animation for timeline items
        const timelineItems = document.querySelectorAll('.timeline-item');
        const featureCards = document.querySelectorAll('.feature-card');
        
        const animateOnScroll = (entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('visible');
                    observer.unobserve(entry.target);
                }
            });
        };
        
        const options = {
            threshold: 0.2,
            rootMargin: '0px 0px -50px 0px'
        };
        
        const observer = new IntersectionObserver(animateOnScroll, options);
        
        timelineItems.forEach(item => {
            observer.observe(item);
        });
        
        featureCards.forEach((card, index) => {
            card.style.transitionDelay = `${index * 0.1}s`;
            observer.observe(card);
        });
        
        // Parallax effect for hero section
        const hero = document.querySelector('.hero');
        const egg = document.querySelector('.floating-egg');
        const coin = document.querySelector('.floating-coin');
        
        window.addEventListener('scroll', () => {
            const scrollPosition = window.pageYOffset;
            
            if (scrollPosition <= hero.offsetHeight) {
                const yPos = scrollPosition * 0.5;
                hero.style.backgroundPosition = `center ${yPos}px`;
                
                egg.style.transform = `translateY(${scrollPosition * 0.4}px) rotate(${scrollPosition * 0.05}deg)`;
                coin.style.transform = `translateY(${scrollPosition * 0.3}px) rotate(-${scrollPosition * 0.08}deg)`;
            }
        });
    </script>
</body>
</html>