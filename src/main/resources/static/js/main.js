document.addEventListener('DOMContentLoaded', function() {

    // Delete Confirmations
    // Looks for any form with a 'data-confirm-delete' attribute
    // or any button with 'data-confirm-delete-message'
    const deleteForms = document.querySelectorAll('form[data-confirm-delete-message]');
    deleteForms.forEach(form => {
        form.addEventListener('submit', function(event) {
            const message = form.getAttribute('data-confirm-delete-message') || 'Are you sure you want to delete this item?';
            if (!confirm(message)) {
                event.preventDefault();
            }
        });
    });

    // Alternative for link-based deletes if forms are not used everywhere
    const deleteButtons = document.querySelectorAll('button[data-confirm-delete-message], a[data-confirm-delete-message]');
     deleteButtons.forEach(button => {
        // If it's a button not of type submit already handled by form listener above
        if (button.tagName === 'A' || (button.tagName === 'BUTTON' && button.type !== 'submit')) {
            button.addEventListener('click', function(event) {
                const message = button.getAttribute('data-confirm-delete-message') || 'Are you sure you want to delete this item?';
                if (!confirm(message)) {
                    event.preventDefault();
                    // If it's an <a> tag, also stop navigation
                    if (button.tagName === 'A') {
                        return false;
                    }
                } else {
                    // If it's an <a> and confirmed, and it's part of a form that should be submitted
                    if (button.tagName === 'A' && button.closest('form')) {
                        // This case is tricky, ideally the 'a' tag should be a submit button
                        // or the JS should more intelligently find and submit the form.
                        // For now, we assume if it's an 'a' tag, it's a direct link or handled elsewhere.
                    }
                }
            });
        }
    });


    // Password Visibility Toggle
    // Looks for any element with class 'password-toggle'
    const passwordToggles = document.querySelectorAll('.password-toggle');
    passwordToggles.forEach(toggle => {
        toggle.addEventListener('click', function() {
            const passwordInputId = this.getAttribute('data-input');
            const passwordInput = document.getElementById(passwordInputId);
            if (passwordInput) {
                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    this.textContent = this.getAttribute('data-text-hide') || 'Hide';
                } else {
                    passwordInput.type = 'password';
                    this.textContent = this.getAttribute('data-text-show') || 'Show';
                }
            }
        });
    });

});

// Helper function to preserve query parameters when switching language (already in layout.html, but good to have here if needed elsewhere)
// function switchLanguage(event, link) {
//     event.preventDefault();
//     var currentUrl = new URL(window.location.href);
//     var targetLang = new URL(link.href).searchParams.get('lang');

//     var searchParams = new URLSearchParams();
//     currentUrl.searchParams.forEach((value, key) => {
//         if (key !== 'lang') {
//             searchParams.append(key, value);
//         }
//     });
//     searchParams.set('lang', targetLang);

//     window.location.href = currentUrl.pathname + '?' + searchParams.toString();
// }
// document.querySelectorAll('.lang-switcher a').forEach(function(link) {
//     link.addEventListener('click', function(event) {
//        switchLanguage(event, this);
//     });
// });
