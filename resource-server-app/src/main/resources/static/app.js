// Inicializa o Keycloak
// Referencia: https://www.keycloak.org/securing-apps/javascript-adapter#_api_reference
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
  const authenticated = await keycloak.init({ onLoad: 'check-sso' /*, pkceMethod: 'S256' */ });
  if (authenticated) {
    document.getElementById('accessToken').value = keycloak.token;
    document.getElementById('idToken').value = keycloak.idToken;
    await showUserInfo();
    document.getElementById('btnPeao').onclick = function() {
      mostrarMensagemController('peao');
    }
    document.getElementById('btnGerente').onclick = function() {
      mostrarMensagemController('gerente');
    }
    document.getElementById('btnDiretor').onclick = function() {
      mostrarMensagemController('diretor');
    }
  }
}

async function mostrarMensagemController(role) {
  if (!keycloak) {
    return;
  }
  const endpointUrl = '/api/' + role;
  try {
    const httpResp = await fetch(endpointUrl, {
      headers: {
        'content-type': 'application/json',
        authorization: 'Bearer ' + keycloak.token
      }
    });
    if (!httpResp.ok) {
      throw new Error(`Status ${httpResp.status}`);
    }
    const dados = await httpResp.json();
    alert(`Mensagem: ${dados.mensagem}`);
  } catch (err) {
    alert(`Erro ao obter dados - ${err}`);
  }

}

document.addEventListener('DOMContentLoaded', doLoad);
document.getElementById('btnLogin').onclick = async () => await keycloak.login();
document.getElementById('btnLogout').onclick = async () => await keycloak.logout();

