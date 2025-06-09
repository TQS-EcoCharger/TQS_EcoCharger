const isDevelopment = window.location.hostname === 'localhost';

const CONFIG = {
    API_URL: isDevelopment 
        ? 'http://localhost:8080/api/'
        : 'http://deti-tqs-19.ua.pt:5000/api/',
};

export default CONFIG;