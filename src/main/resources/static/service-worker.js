self.addEventListener('install', function(event) {
  console.log('📦 Service Worker instalado');
  self.skipWaiting();
});

self.addEventListener('activate', function(event) {
  console.log('⚡ Service Worker activado');
});

self.addEventListener('fetch', function(event) {
  // Puede manejar peticiones si deseas
});
