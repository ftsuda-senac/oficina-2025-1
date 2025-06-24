// Inicializa o Keycloak
const keycloakConfig = {
  url: 'http://localhost:9090',       // URL do seu servidor Keycloak
  realm: 'oficina',                   // Nome do seu Realm
  clientId: 'oficina-client'          // ID do cliente configurado
};
const keycloak = new Keycloak(keycloakConfig);

async function showUserInfo() {
  const user = await keycloak.loadUserInfo();
  document.getElementById('username').textContent = user.preferred_username;
  document.getElementById('email').textContent = user.email;
  document.getElementById('comLogin').style.display = 'block';
  document.getElementById('semLogin').style.display = 'none';
}

async function doLoad() {
  const authenticated = await keycloak.init({ onLoad: 'check-sso', pkceMethod: 'S256' });
  if (authenticated) {
    document.getElementById('accessToken').value = keycloak.token;
    document.getElementById('idToken').value = keycloak.idToken;
    await showUserInfo();
  }
}

document.addEventListener('DOMContentLoaded', doLoad);
document.getElementById('btnLogin').onclick = async () => await keycloak.login();
document.getElementById('btnLogout').onclick = async () => await keycloak.logout();

