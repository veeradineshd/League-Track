function addTeam() {
    run(async () => {
        const tid = getTournamentId();
        if (!tid) throw new Error('Select a tournament first, on the Tournament page');
        await api(`/tournaments/${tid}/teams`, 'POST', {
            name: document.getElementById('teamName').value
        });
        document.getElementById('teamName').value = '';
        toast('Team registered');
        await loadTeams();
    });
}

async function loadTeams() {
    const box = document.getElementById('teams');
    const tid = getTournamentId();
    if (!tid) { box.innerHTML = '<div class="empty">Select a tournament first.</div>'; return; }
    const teams = await api(`/tournaments/${tid}/teams`);
    box.innerHTML = teams.length
        ? teams.map(t => `<span class="chip">${esc(t.name)}</span>`).join('')
        : '<div class="empty">No teams registered yet.</div>';
}

run(loadTeams);