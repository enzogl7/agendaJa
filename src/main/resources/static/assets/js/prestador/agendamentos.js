const meses = [
"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
"Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
];
let dataAtual = new Date();
const feriadosFixos = [
"01-01",
"04-21",
"05-01",
"09-07",
"10-12",
"11-02",
"11-15",
"12-25"
];

function gerarCalendario(data) {
    const ano = data.getFullYear();
    const mes = data.getMonth();

    document.getElementById("textoMesAgenda").textContent = `${meses[mes]} ${ano}`;

    const primeiroDia = new Date(ano, mes, 1);
    const ultimoDia = new Date(ano, mes + 1, 0);
    const diaSemanaInicio = primeiroDia.getDay();
    const diasNoMes = ultimoDia.getDate();

    const calendarDays = document.getElementById("calendar-days");
    calendarDays.innerHTML = "";

    for (let i = 0; i < diaSemanaInicio; i++) {
        const div = document.createElement("div");
        calendarDays.appendChild(div);
    }

    for (let dia = 1; dia <= diasNoMes; dia++) {
        const div = document.createElement("div");
        div.textContent = dia;

        const mm = String(mes + 1).padStart(2, '0');
        const dd = String(dia).padStart(2, '0');
        const dataFormatada = `${mm}-${dd}`;

        if (feriadosFixos.includes(dataFormatada)) {
            div.classList.add("bg-[#E74C3C]", "text-white", "rounded-lg", "py-2");
            div.title = "Feriado";
        } else {
            div.classList.add("bg-[#f1f1f1]", "rounded-lg", "py-2", "hover:bg-[#7F8C8D]", "hover:text-white", "transition");
        }

        calendarDays.appendChild(div);
    }
}

function mudarMes(offset) {
    dataAtual.setMonth(dataAtual.getMonth() + offset);
    gerarCalendario(dataAtual);
}

document.addEventListener("DOMContentLoaded", () => {
    gerarCalendario(dataAtual);
});
