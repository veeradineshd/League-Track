function generateFixture() {
    run(async () => {
        const tid = getTournamentId();
        if (!tid) throw new Error('Select a tournament first, on the Tournament page');
        await api(`/tournaments/${tid}/fixtures`, 'POST');
        toast('Fixture generated');
        await loadFixture();
    });
}

async function loadFixture() {
    const box = document.getElementById('fixture');
    const tid = getTournamentId();
    if (!tid) { box.innerHTML = '<div class="empty">Select a tournament first.</div>'; return; }

    let matches;
    try {
        matches = await api(`/tournaments/${tid}/fixtures`);
    } catch (e) {
        box.innerHTML = '<div class="empty">Fixture not generated yet. Click the button above.</div>';
        return;
    }

    let html = '<table><tr><th class="left">Home</th><th colspan="3">Score</th>'
        + '<th class="left">Away</th><th>Status</th><th>Winner</th><th></th></tr>';
    let round = 0;
    for (const m of matches) {
        if (m.round !== round) {
            round = m.round;
            html += `<tr><td class="round" colspan="8">Round ${round}</td></tr>`;
        }
        const hs = m.homeScore ?? '';
        const as = m.awayScore ?? '';
        html += `<tr>
            <td class="left">${esc(m.homeTeam)}</td>
            <td><input class="score" type="number" min="0" id="h${m.id}" value="${hs}"></td>
            <td>-</td>
            <td><input class="score" type="number" min="0" id="a${m.id}" value="${as}"></td>
            <td class="left">${esc(m.awayTeam)}</td>
            <td><span class="badge ${m.status}">${m.status}</span></td>
            <td>${m.winner ? esc(m.winner) : '-'}</td>
            <td><button class="small" onclick="saveResult(${m.id}, '${m.status}')">
                ${m.status === 'COMPLETED' ? 'Update' : 'Save'}</button></td>
        </tr>`;
    }
    box.innerHTML = html + '</table>';
}

function saveResult(matchId, status) {
    run(async () => {
        const h = document.getElementById('h' + matchId).value;
        const a = document.getElementById('a' + matchId).value;
        if (h === '' || a === '') throw new Error('Enter both scores');
        const body = { homeScore: parseInt(h), awayScore: parseInt(a) };
        await api(`/matches/${matchId}/result`, status === 'COMPLETED' ? 'PUT' : 'POST', body);
        toast(status === 'COMPLETED' ? 'Result corrected' : 'Result recorded');
        await loadFixture();
    });
}

run(loadFixture);