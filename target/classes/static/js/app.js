// =============================================
//   SOCIALMEDIA APP - JAVASCRIPT
// =============================================

document.addEventListener('DOMContentLoaded', function () {

    // ---- Character counter for post textarea ----
    const postContent = document.getElementById('postContent');
    const charCount = document.getElementById('charCount');

    if (postContent && charCount) {
        postContent.addEventListener('input', function () {
            const len = this.value.length;
            charCount.textContent = len;
            charCount.style.color = len > 450 ? '#EF4444' : len > 400 ? '#F59E0B' : '#64748B';
        });
    }

    // ---- Auto-dismiss alerts after 4 seconds ----
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-8px)';
            setTimeout(() => alert.remove(), 400);
        }, 4000);
    });

    // ---- Animate post cards staggered ----
    const postCards = document.querySelectorAll('.post-card');
    postCards.forEach(function (card, index) {
        card.style.animationDelay = (index * 0.06) + 's';
    });

    // ---- Confirm delete for posts ----
    const deleteForms = document.querySelectorAll('form[onsubmit]');
    // Already handled inline via onsubmit attribute

    // ---- Active nav link highlight ----
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-links a');
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });

    // ---- Smooth scroll to comments if hash ----
    if (window.location.hash === '#comments') {
        const commentsSection = document.querySelector('.comments-section');
        if (commentsSection) {
            setTimeout(() => commentsSection.scrollIntoView({ behavior: 'smooth' }), 300);
        }
    }

    // ---- Tooltip on post time hover ----
    const timeTags = document.querySelectorAll('.post-time');
    timeTags.forEach(tag => {
        tag.title = 'Posted: ' + tag.textContent;
    });

    console.log('%c SocialMedia App', 'color: #4F46E5; font-weight: bold; font-size: 16px;');
    console.log('%c Built with Java Spring Boot + Thymeleaf', 'color: #64748B;');
});

// ---- Copy post link to clipboard ----
function copyLink(btn) {
    const url = window.location.origin + btn.getAttribute('data-url');
    navigator.clipboard.writeText(url).then(function () {
        const icon = btn.querySelector('i');
        icon.className = 'fas fa-check';
        btn.style.color = '#10B981';
        setTimeout(function () {
            icon.className = 'fas fa-share';
            btn.style.color = '';
        }, 2000);
    }).catch(function () {
        prompt('Copy this link:', url);
    });
}
