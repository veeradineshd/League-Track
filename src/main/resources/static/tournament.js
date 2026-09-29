async function loadTournaments(selectId) {
    const list = await api('/tournaments');
    const sel = document.getElementById('tournamentSelect');

    sel.innerHTML = list.length
        ? list.map(t => `<option value="${t.id}">${esc(t.name)} (${esc(t.sport)})</option>`).join('')
        : '<option value="">No tournaments yet</option>';

    if (list.length) {
        let id = selectId || getTournamentId();
        if (!list.some(t => t.id == id)) id = list[0].id;
        sel.value = id;
        setTournamentId(id);
    }

    renderAllTournaments(list);
    await refreshCurrentTournamentLabel();
}

document.getElementById('tournamentSelect').addEventListener('change', e => {
    setTournamentId(e.target.value);
    run(refreshCurrentTournamentLabel);
});

function renderAllTournaments(list) {
    const box = document.getElementById('allTournaments');
    if (!list.length) { box.innerHTML = '<div class="empty">No tournaments yet.</div>'; return; }
    let html = '<table><tr><th class="left">Name</th><th class="left">Sport</th>'
        + '<th>Win</th><th>Draw</th><th>Loss</th><th>Fixture Generated</th></tr>';
    list.forEach(t => {
        html += `<tr>
            <td class="left">${esc(t.name)}</td>
            <td class="left">${esc(t.sport)}</td>
            <td>${t.winPoints}</td><td>${t.drawPoints}</td><td>${t.lossPoints}</td>
            <td>${t.fixtureGenerated ? 'Yes' : 'No'}</td>
        </tr>`;
    });
    box.innerHTML = html + '</table>';
}

function createTournament() {
    run(async () => {
        const body = {
            name: document.getElementById('tName').value,
            sport: document.getElementById('tSport').value,
            winPoints: parseInt(document.getElementById('tWin').value),
            drawPoints: parseInt(document.getElementById('tDraw').value),
            lossPoints: parseInt(document.getElementById('tLoss').value)
        };
        const t = await api('/tournaments', 'POST', body);
        toast('Tournament created');
        document.getElementById('tName').value = '';
        document.getElementById('tSport').value = '';
        await loadTournaments(t.id);
    });
}

run(() => loadTournaments());