/**
 * SUS Smart Assistant — Shared JS utilities
 */
const API_BASE = '/api/v1';

// ── Token management ──
function getToken() {
  return localStorage.getItem('sus_token');
}

function setToken(token) {
  localStorage.setItem('sus_token', token);
}

function clearToken() {
  localStorage.removeItem('sus_token');
  localStorage.removeItem('sus_user');
}

function getUser() {
  try { return JSON.parse(localStorage.getItem('sus_user')); } catch { return null; }
}

function setUser(user) {
  localStorage.setItem('sus_user', JSON.stringify(user));
}

function decodeToken(token) {
  try {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  } catch { return {}; }
}

function getUserRole() {
  const user = getUser();
  return user ? user.role : null;
}

function requireAuth() {
  if (!getToken()) {
    window.location.href = 'index.html';
    return false;
  }
  return true;
}

function requireRole(allowedRoles) {
  const role = getUserRole();
  if (!allowedRoles.includes(role)) {
    window.location.href = getHomePageForRole(role);
    return false;
  }
  return true;
}

function getHomePageForRole(role) {
  switch (role) {
    case 'PACIENTE': return 'dashboard.html';
    case 'GESTOR': return 'metricas.html';
    case 'PROFISSIONAL':
    default: return 'dashboard.html';
  }
}

function logout() {
  clearToken();
  window.location.href = 'index.html';
}

// ── API helpers ──
async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (token) headers['Authorization'] = 'Bearer ' + token;
  const res = await fetch(API_BASE + path, { ...options, headers });
  if (res.status === 401) {
    clearToken();
    window.location.href = 'index.html';
    throw new Error('Sessão expirada');
  }
  return res;
}

async function apiGet(path) {
  const res = await apiFetch(path);
  if (!res.ok) {
    const err = await res.json().catch(() => ({ mensagem: 'Erro desconhecido' }));
    throw new Error(err.mensagem || `Erro ${res.status}`);
  }
  return res.json();
}

async function apiPost(path, body) {
  const res = await apiFetch(path, { method: 'POST', body: JSON.stringify(body) });
  return res;
}

async function apiPatch(path, body) {
  const res = await apiFetch(path, { method: 'PATCH', body: JSON.stringify(body) });
  return res;
}

// ── UI helpers ──
function showAlert(id, msg, type) {
  const el = document.getElementById(id);
  if (!el) return;
  el.className = 'alert show alert-' + type;
  el.textContent = msg;
}

function hideAlert(id) {
  const el = document.getElementById(id);
  if (el) el.className = 'alert';
}

function formatDate(dateStr) {
  if (!dateStr) return '—';
  const d = new Date(dateStr);
  return d.toLocaleDateString('pt-BR');
}

function formatDateTime(dateStr) {
  if (!dateStr) return '—';
  const d = new Date(dateStr);
  return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
}

function gravidadeBadge(g) {
  const map = { CRITICA: 'critica', MODERADA: 'moderada', LEVE: 'leve' };
  return `<span class="badge badge-${map[g] || 'leve'}">${g}</span>`;
}

function confiancaBadge(c) {
  const map = { ALTA: 'alta', MEDIA: 'media', BAIXA: 'baixa' };
  return `<span class="badge badge-${map[c] || 'media'}">${c}</span>`;
}

// ── Navbar renderer (role-aware) ──
function renderNavbar(activePage) {
  const nav = document.getElementById('navbar');
  if (!nav) return;
  const role = getUserRole();
  let links = '';
  switch (role) {
    case 'PROFISSIONAL':
      links = `
        <a href="dashboard.html" class="${activePage === 'dashboard' ? 'active' : ''}">Prontuário</a>
        <a href="assistente.html" class="${activePage === 'assistente' ? 'active' : ''}">Assistente IA</a>`;
      break;
    case 'PACIENTE':
      links = `
        <a href="dashboard.html" class="${activePage === 'dashboard' ? 'active' : ''}">Meu Prontuário</a>`;
      break;
    case 'GESTOR':
      links = `
        <a href="metricas.html" class="${activePage === 'metricas' ? 'active' : ''}">Métricas</a>`;
      break;
    default:
      links = '<a href="dashboard.html">Início</a>';
  }
  const user = getUser();
  const userLabel = user ? `<span class="nav-user">${user.username} (${role})</span>` : '';
  nav.innerHTML = `
    <div class="logo"><span>🏥</span> SUS Smart Assistant</div>
    <nav>
      ${links}
      ${userLabel}
      <button class="btn-logout" onclick="logout()">Sair</button>
    </nav>
  `;
}
