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

$(document).ready(function () {
    paginacaoTabela('tableAgendamentosPrestador');
});

document.addEventListener("DOMContentLoaded", () => {
    gerarCalendario(dataAtual);
});

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
                modalNovoAgendamento(dataCompleta);
            });
        }

        calendarDays.appendChild(div);
    }
}

function mudarMes(offset) {
    dataAtual.setMonth(dataAtual.getMonth() + offset);
    gerarCalendario(dataAtual);
}

function modalNovoAgendamento(data) {
    $('#modalNovoAgendamento').modal('show');
    if (data != null) {
        $('#dataAgendamento').val(data);
    }
}

function salvarAgendamento() {
    var servico = document.getElementById('servicoAgendamento').value;
    var data = document.getElementById('dataAgendamento').value;
    var horario = document.getElementById('horarioAgendamento').value;
    var status = document.getElementById('statusAgendamento').value;

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
        url: '/prestador/salvaragendamento',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            servico: servico, data: data, horario: horario, status: status
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Agendamento criado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    });
                    break;
                case 409:
                    Swal.fire({
                        title: "Ops!",
                        text: "Você atingiu seu limite de 10 agendamentos do plano básico de assinatura!",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    });
                    return;
                default:
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao salvar o agendamento.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}

function excluirAgendamento(button) {
    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir este agendamento?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/prestador/excluiragendamento',
                type: 'POST',
                data: {
                    idAgendamento: button.getAttribute('data-id')
                },
                complete: function(xhr, status) {
                    switch (xhr.status) {
                        case 200:
                            Swal.fire({
                                title: "Pronto!",
                                text: "Excluído com sucesso!",
                                icon: "success",
                                confirmButtonText: 'OK'
                            }).then(() => {
                                location.reload();
                            });
                            break;
                        default:
                            Swal.fire({
                                title: "Ops!",
                                text: "Ocorreu um erro ao excluir o agendamento.",
                                icon: "error",
                                confirmButtonText: "Ok"
                            });
                            return;
                    }
                }
            });
        }
    })
}

function modalEditarAgendamento(button) {
    $('#modalEditarAgendamento').modal('show');
    $('#idAgendamentoEdicao').val(button.getAttribute('data-id'));
    $('#servicoAgendamentoEdicao').val(button.getAttribute('data-servico'));
    $('#dataAgendamentoEdicao').val(button.getAttribute('data-data'));
    $('#horarioAgendamentoEdicao').val(button.getAttribute('data-horario'));
    $('#statusAgendamentoEdicao').val(button.getAttribute('data-status'));
}

function salvarEdicaoAgendamento() {
    var id = document.getElementById('idAgendamentoEdicao').value;
    var servico = document.getElementById('servicoAgendamentoEdicao').value;
    var data = document.getElementById('dataAgendamentoEdicao').value;
    var horario = document.getElementById('horarioAgendamentoEdicao').value;
    var status = document.getElementById('statusAgendamentoEdicao').value;

    if (!servico || !data || !horario) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/prestador/editaragendamento',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id:id, servico: servico, data: data, horario: horario, status: status
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Agendamento editado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    });
                    break;
                default:
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao editar o agendamneto.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}

function confirmarAgendamento(button) {
    Swal.fire({
        icon: 'info',
        title: 'Deseja confirmar este agendamento?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/prestador/confirmaragendamento',
                type: 'POST',
                data: {
                    idAgendamento: button.getAttribute('data-id')
                },
                complete: function(xhr, status) {
                    switch (xhr.status) {
                        case 200:
                            Swal.fire({
                                title: "Pronto!",
                                text: "Confirmado com sucesso!",
                                icon: "success",
                                confirmButtonText: 'OK'
                            }).then(() => {
                                location.reload();
                            });
                            break;
                        default:
                            Swal.fire({
                                title: "Ops!",
                                text: "Ocorreu um erro ao confirmar o agendamento.",
                                icon: "error",
                                confirmButtonText: "Ok"
                            });
                            return;
                    }
                }
            });
        }
    })
}

function cancelarAgendamento(button) {
    Swal.fire({
        icon: 'info',
        title: 'Deseja cancelar este agendamento?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/prestador/cancelaragendamento',
                type: 'POST',
                data: {
                    idAgendamento: button.getAttribute('data-id')
                },
                complete: function(xhr, status) {
                    switch (xhr.status) {
                        case 200:
                            Swal.fire({
                                title: "Pronto!",
                                text: "Cancelado com sucesso!",
                                icon: "success",
                                confirmButtonText: 'OK'
                            }).then(() => {
                                location.reload();
                            });
                            break;
                        default:
                            Swal.fire({
                                title: "Ops!",
                                text: "Ocorreu um erro ao cancelar o agendamento.",
                                icon: "error",
                                confirmButtonText: "Ok"
                            });
                            return;
                    }
                }
            });
        }
    })
}