async function loadStandings() {
    const box = document.getElementById('standings');
    const tid = getTournamentId();
    if (!tid) { box.innerHTML = '<div class="empty">Select a tournament first.</div>'; return; }

    const rows = await api(`/tournaments/${tid}/standings`);
    if (!rows.length) { box.innerHTML = '<div class="empty">No teams yet.</div>'; return; }

    let html = '<table><tr><th>#</th><th class="left">Team</th><th>P</th><th>W</th><th>D</th>'
        + '<th>L</th><th>GF</th><th>GA</th><th>GD</th><th>Pts</th></tr>';
    rows.forEach((r, i) => {
        html += `<tr class="${i === 0 && r.played > 0 ? 'top' : ''}">
            <td>${r.rank}</td><td class="left">${esc(r.team)}</td><td>${r.played}</td>
            <td>${r.won}</td><td>${r.drawn}</td><td>${r.lost}</td>
            <td>${r.goalsFor}</td><td>${r.goalsAgainst}</td>
            <td>${r.goalDifference}</td><td><b>${r.points}</b></td></tr>`;
    });
    box.innerHTML = html + '</table>';
}

run(loadStandings);