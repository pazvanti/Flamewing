// Flamewing Documentation Interactive Helper

document.addEventListener('DOMContentLoaded', () => {
    // 1. Back to top button
    const backToTopBtn = document.createElement('button');
    backToTopBtn.className = 'back-to-top';
    backToTopBtn.setAttribute('aria-label', 'Back to top');
    backToTopBtn.innerHTML = '<i class="bi bi-chevron-up"></i>';
    document.body.appendChild(backToTopBtn);

    window.addEventListener('scroll', () => {
        if (window.scrollY > 300) {
            backToTopBtn.classList.add('show');
        } else {
            backToTopBtn.classList.remove('show');
        }
    });

    backToTopBtn.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });

    // 2. Add copy button to pre code blocks if not already present
    document.querySelectorAll('pre code').forEach((codeBlock) => {
        const pre = codeBlock.parentElement;
        if (pre && !pre.querySelector('.copy-code-btn')) {
            const copyBtn = document.createElement('button');
            copyBtn.className = 'copy-code-btn';
            copyBtn.type = 'button';
            copyBtn.setAttribute('title', 'Copy code');
            copyBtn.innerHTML = '<i class="bi bi-clipboard"></i> <span>Copy</span>';

            copyBtn.addEventListener('click', async () => {
                const text = codeBlock.innerText;
                try {
                    await navigator.clipboard.writeText(text);
                    copyBtn.innerHTML = '<i class="bi bi-check2 text-success"></i> <span class="text-success">Copied!</span>';
                    setTimeout(() => {
                        copyBtn.innerHTML = '<i class="bi bi-clipboard"></i> <span>Copy</span>';
                    }, 2000);
                } catch (err) {
                    // Fallback
                    const textArea = document.createElement('textarea');
                    textArea.value = text;
                    document.body.appendChild(textArea);
                    textArea.select();
                    document.execCommand('copy');
                    document.body.removeChild(textArea);
                    copyBtn.innerHTML = '<i class="bi bi-check2 text-success"></i> <span class="text-success">Copied!</span>';
                    setTimeout(() => {
                        copyBtn.innerHTML = '<i class="bi bi-clipboard"></i> <span>Copy</span>';
                    }, 2000);
                }
            });

            pre.style.position = 'relative';
            copyBtn.style.position = 'absolute';
            copyBtn.style.top = '10px';
            copyBtn.style.right = '12px';
            copyBtn.style.zIndex = '5';
            pre.appendChild(copyBtn);
        }
    });

    // 3. Elements sidebar search filter (if on elements.html)
    const searchInput = document.getElementById('sidebar-search');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            const items = document.querySelectorAll('.elements_ul .elements_li');
            items.forEach((item) => {
                const text = item.textContent.toLowerCase();
                if (text.includes(query)) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    }

    // 4. Highlight active link in sidebar on scroll (elements.html)
    const sidebarLinks = document.querySelectorAll('.elements_ul a');
    if (sidebarLinks.length > 0) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const id = entry.target.getAttribute('id');
                    if (id) {
                        sidebarLinks.forEach(link => {
                            if (link.getAttribute('href') === `#${id}`) {
                                link.classList.add('active');
                            } else {
                                link.classList.remove('active');
                            }
                        });
                    }
                }
            });
        }, { rootMargin: '-20% 0px -70% 0px' });

        document.querySelectorAll('[id]').forEach(section => {
            observer.observe(section);
        });
    }

    // 5. Open documentation dropdown menu on hover on desktop
    const dropdownElements = document.querySelectorAll('.navbar .nav-item.dropdown');
    dropdownElements.forEach((dropdown) => {
        const toggle = dropdown.querySelector('[data-bs-toggle="dropdown"]');
        if (!toggle) return;

        let hideTimeout = null;

        dropdown.addEventListener('mouseenter', () => {
            if (window.innerWidth >= 992) {
                clearTimeout(hideTimeout);
                if (window.bootstrap && bootstrap.Dropdown) {
                    const dropdownInstance = bootstrap.Dropdown.getOrCreateInstance(toggle);
                    dropdownInstance.show();
                }
            }
        });

        dropdown.addEventListener('mouseleave', () => {
            if (window.innerWidth >= 992) {
                hideTimeout = setTimeout(() => {
                    if (window.bootstrap && bootstrap.Dropdown) {
                        const dropdownInstance = bootstrap.Dropdown.getInstance(toggle);
                        if (dropdownInstance) {
                            dropdownInstance.hide();
                        }
                    }
                }, 150);
            }
        });
    });
});

