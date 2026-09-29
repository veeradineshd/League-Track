const API = '/api';

/* ---------- generic API call ---------- */
async function api(path, method = 'GET', body) {
    const res = await fetch(API + path, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: body ? JSON.stringify(body) : undefined
    });
    const text = await res.text();
    let data = null;
    if (text) {
        try { data = JSON.parse(text); } catch (e) { /* not JSON */ }
    }
    if (!res.ok) {
        throw new Error(data && data.message ? data.message : 'Request failed (' + res.status + ')');
    }
    return data;
}

function esc(s) {
    return String(s).replace(/[&<>"']/g, c => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    }[c]));
}

let toastTimer;
function toast(msg, ok = true) {
    const t = document.getElementById('toast');
    if (!t) return;
    t.textContent = msg;
    t.className = ok ? 'ok' : 'err';
    t.style.display = 'block';
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => t.style.display = 'none', 3500);
}

async function run(fn) {
    try { await fn(); } catch (e) { toast(e.message, false); }
}

/* ---------- selected tournament, shared across pages ---------- */
function getTournamentId() {
    return localStorage.getItem('tournamentId');
}
function setTournamentId(id) {
    localStorage.setItem('tournamentId', id);
}

/* ---------- nav bar: highlight current page + show tournament name ---------- */
function initNav() {
    const page = document.body.dataset.page;
    document.querySelectorAll('nav a').forEach(a => {
        if (a.dataset.page === page) a.classList.add('active');
    });
    run(refreshCurrentTournamentLabel);
}

async function refreshCurrentTournamentLabel() {
    const box = document.getElementById('currentTournament');
    if (!box) return;
    const id = getTournamentId();
    if (!id) { box.innerHTML = 'No tournament selected. Go to <a href="index.html">Tournament</a> to create or pick one.'; return; }
    try {
        const t = await api(`/tournaments/${id}`);
        box.innerHTML = `Current tournament: <b>${esc(t.name)}</b> (${esc(t.sport)})`;
    } catch (e) {
        box.innerHTML = 'Saved tournament not found. Go to <a href="index.html">Tournament</a> to pick one.';
    }
}

document.addEventListener('DOMContentLoaded', initNav);