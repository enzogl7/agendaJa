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

document.addEventListener("DOMContentLoaded", () => {
    gerarCalendario(dataAtual);
    flatpickr("#dataAgendamentoCliente", {
        dateFormat: "d/m/Y",
        locale: "pt",
        minDate: "today",
    });
});

document.getElementsByClassName("tabcontent")[0].style.display = "block";
document.querySelector("button").classList.add("py-2", "transition-all", "duration-300", "ease-in-out", "border-b-2", "border-[#27AE60]", "text-[#27AE60]", "hover:border-[#27AE60]", "hover:text-[#27AE60]", "focus:outline-none", "focus:border-[#27AE60]");


function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    tablinks = document.getElementsByTagName("button");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].classList.remove("text-[#27AE60]", "border-[#27AE60]", "py-2", "transition-all", "duration-300", "ease-in-out", "border-b-2", "border-transparent", "hover:border-[#27AE60]", "hover:text-[#27AE60]", "focus:outline-none", "focus:border-[#27AE60]");
    }

    document.getElementById(tabName).style.display = "block";

    evt.currentTarget.classList.add("py-2", "transition-all", "duration-300", "ease-in-out", "border-b-2", "border-[#27AE60]", "text-[#27AE60]", "hover:border-[#27AE60]", "hover:text-[#27AE60]", "focus:outline-none", "focus:border-[#27AE60]");
}

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
        const dataCompleta = `${ano}-${mm}-${dd}`;

        if (feriadosFixos.includes(`${mm}-${dd}`)) {
            div.classList.add("bg-[#E74C3C]", "text-white", "rounded-lg", "py-2");
            div.title = "Feriado";
        } else if (datasAgendadas.includes(dataCompleta)) {
            div.classList.add("bg-[#27AE60]", "text-white", "font-semibold", "rounded-lg", "py-2");
            div.title = "Agendamento";
        } else {
            div.classList.add(
                "bg-[#f1f1f1]",
                "rounded-lg",
                "py-2",
                "hover:bg-[#7F8C8D]",
                "hover:text-white",
                "transition",
                "cursor-pointer"
            );
            div.title = "Clique para adicionar um serviço";
            div.addEventListener("click", () => {
                const inputData = document.getElementById("dataAgendamento");
                if (inputData) {
                    inputData.value = dataCompleta;
                }
                modalNovoAgendamentoCliente(dataCompleta);
            });
        }

        calendarDays.appendChild(div);
    }
}

function mudarMes(offset) {
    dataAtual.setMonth(dataAtual.getMonth() + offset);
    gerarCalendario(dataAtual);
}

function modalNovoAgendamentoCliente(data, button) {
    $('#modalNovoAgendamentoCliente').modal('show');
    if (data != null) {
        $('#dataAgendamento').val(data);
    }
    $('#prestador').val(button.getAttribute('data-prestador'));
}

function salvarAgendamentoCliente() {
    var servico = document.getElementById('servicoAgendamentoCliente').value;
    var data = document.getElementById('dataAgendamentoCliente').value;
    console.log("DAT: " + data)
    var horario = document.getElementById('horarioAgendamentoCliente').value;
    var pagamento = document.getElementById('formaPagamentoCliente').value;
    var prestador = document.getElementById('prestador').value;

    if (!servico || !data || !horario) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    var hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    var partesData = data.split("-");
    var dataSelecionada = new Date(partesData[0], partesData[1] - 1, partesData[2]);
    dataSelecionada.setHours(0, 0, 0, 0);
    if (dataSelecionada < hoje) {
        Swal.fire({
            title: "Data inválida",
            text: "A data do agendamento não pode ser anterior a hoje!",
            icon: "error",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/negocio/salvaragendamentocliente',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            servico: servico, data: data, horario: horario, pagamento: pagamento, prestador: prestador
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Seu agendamento foi solicitado com sucesso e está com status pendente." +
                            "\nVocê será notificado por e-mail caso seja confirmado pelo estabelecimento.",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    });
                    break;
                case 401:
                    Swal.fire({
                        title: "Faça login para continuar",
                        text: "Você precisa estar logado para agendar um serviço. Clique em OK para acessar ou criar sua conta.",
                        icon: "info",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/login";
                        }
                    });
                    return;
                case 409:
                    Swal.fire({
                        title: "Ops!",
                        text: "Este estabelecimento não está aceitando agendamentos no momento.",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    });
                    return;
                default:
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao finalizar o agendamento.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}

function atualizarHorariosDisponiveis() {
    const dataSelecionada = document.getElementById('dataAgendamentoCliente').value;
    const horarioSelect = document.getElementById('horarioAgendamentoCliente');

    horarioSelect.innerHTML = '';

    if (dataSelecionada && horariosPorData[dataSelecionada]) {
        const horariosAgendados = horariosPorData[dataSelecionada];
        const horariosDisponiveisFiltrados = horariosDisponiveis.filter(horario => !horariosAgendados.includes(horario));

        horariosDisponiveisFiltrados.forEach(horario => {
            const option = document.createElement('option');
            option.value = horario;
            option.textContent = horario;
            horarioSelect.appendChild(option);
        });
    } else {
        horariosDisponiveis.forEach(horario => {
            const option = document.createElement('option');
            option.value = horario;
            option.textContent = horario;
            horarioSelect.appendChild(option);
        });
    }
}
