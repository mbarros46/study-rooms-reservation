// Sistema de Reservas - JavaScript Functions

document.addEventListener('DOMContentLoaded', function() {
    // Add fade-in animation to main content
    const mainContent = document.querySelector('main, .container');
    if (mainContent) {
        mainContent.classList.add('fade-in');
    }
    
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
    
    // Form validation enhancement
    const forms = document.querySelectorAll('form');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                
                // Show error message
                showNotification('Por favor, preencha todos os campos obrigatórios.', 'error');
            } else {
                // Show loading state
                const submitBtn = form.querySelector('button[type="submit"]');
                if (submitBtn) {
                    const originalText = submitBtn.innerHTML;
                    submitBtn.innerHTML = '<span class="loading-spinner me-2"></span>Processando...';
                    submitBtn.disabled = true;
                    
                    // Re-enable after 3 seconds (in case of error)
                    setTimeout(function() {
                        submitBtn.innerHTML = originalText;
                        submitBtn.disabled = false;
                    }, 3000);
                }
            }
            form.classList.add('was-validated');
        });
    });
    
    // Enhanced datetime inputs
    const datetimeInputs = document.querySelectorAll('input[type="datetime-local"]');
    datetimeInputs.forEach(function(input) {
        // Set minimum date to today
        const now = new Date();
        const minDate = now.toISOString().slice(0, 16);
        input.min = minDate;
        
        // Add validation for business hours (optional)
        input.addEventListener('change', function() {
            const selectedDate = new Date(this.value);
            const hours = selectedDate.getHours();
            
            // Example: warn if outside business hours (6 AM - 10 PM)
            if (hours < 6 || hours > 22) {
                showNotification('Horário selecionado fora do horário de funcionamento recomendado (6h às 22h).', 'warning');
            }
        });
    });
    
    // Smooth scrolling for anchor links
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    anchorLinks.forEach(function(link) {
        link.addEventListener('click', function(e) {
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                e.preventDefault();
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // Auto-update time display
    updateTimeDisplay();
    setInterval(updateTimeDisplay, 1000);
});

// Notification system
function showNotification(message, type = 'info') {
    const alertClass = {
        'success': 'alert-success',
        'error': 'alert-danger',
        'warning': 'alert-warning',
        'info': 'alert-info'
    };
    
    const iconClass = {
        'success': 'bi-check-circle-fill',
        'error': 'bi-exclamation-triangle-fill',
        'warning': 'bi-exclamation-triangle-fill',
        'info': 'bi-info-circle-fill'
    };
    
    const alertHTML = `
        <div class="alert ${alertClass[type]} alert-dismissible fade show" role="alert">
            <i class="bi ${iconClass[type]} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    // Find container for alerts
    let alertContainer = document.querySelector('.alert-container');
    if (!alertContainer) {
        alertContainer = document.createElement('div');
        alertContainer.className = 'alert-container position-fixed top-0 end-0 p-3';
        alertContainer.style.zIndex = '1050';
        document.body.appendChild(alertContainer);
    }
    
    alertContainer.insertAdjacentHTML('beforeend', alertHTML);
    
    // Auto-remove after 5 seconds
    setTimeout(function() {
        const alert = alertContainer.lastElementChild;
        if (alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }
    }, 5000);
}

// Update current time display
function updateTimeDisplay() {
    const timeDisplays = document.querySelectorAll('.current-time');
    const now = new Date();
    const timeString = now.toLocaleTimeString('pt-BR');
    
    timeDisplays.forEach(function(display) {
        display.textContent = timeString;
    });
}

// Format date for display
function formatDate(date) {
    if (typeof date === 'string') {
        date = new Date(date);
    }
    
    return date.toLocaleDateString('pt-BR', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Validate time range
function validateTimeRange(startTime, endTime) {
    const start = new Date(startTime);
    const end = new Date(endTime);
    const now = new Date();
    
    const errors = [];
    
    if (start < now) {
        errors.push('A data de início não pode ser no passado.');
    }
    
    if (end <= start) {
        errors.push('A data de término deve ser posterior à data de início.');
    }
    
    const duration = (end - start) / (1000 * 60 * 60); // hours
    if (duration > 8) {
        errors.push('A reserva não pode durar mais de 8 horas.');
    }
    
    if (duration < 0.5) {
        errors.push('A reserva deve durar pelo menos 30 minutos.');
    }
    
    return errors;
}

// Enhanced form submission with validation
function handleReservationForm(form) {
    const formData = new FormData(form);
    const startTime = formData.get('startAtIso');
    const endTime = formData.get('endAtIso');
    
    if (startTime && endTime) {
        const errors = validateTimeRange(startTime, endTime);
        
        if (errors.length > 0) {
            errors.forEach(error => showNotification(error, 'error'));
            return false;
        }
    }
    
    return true;
}

// Copy to clipboard functionality
function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(function() {
        showNotification('Texto copiado para a área de transferência!', 'success');
    }).catch(function(err) {
        showNotification('Erro ao copiar texto.', 'error');
    });
}

// Dark mode toggle (future enhancement)
function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDark);
    
    showNotification(
        isDark ? 'Modo escuro ativado' : 'Modo claro ativado', 
        'info'
    );
}

// Load dark mode preference
if (localStorage.getItem('darkMode') === 'true') {
    document.body.classList.add('dark-mode');
}