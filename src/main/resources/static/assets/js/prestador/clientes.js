$(document).ready(function () {
    paginacaoTabela('tableClientesPrestador');
});


function novoClienteModal() {
    $('#modalNovoCliente').modal('show');
}

function modalEditarCliente(button) {
    $('#modalClienteEdicao').modal('show');
    $('#idClienteEdicao').val(button.getAttribute('data-id'));
    $('#nomeClienteEdicao').val(button.getAttribute('data-nome'));
    $('#emailClienteEdicao').val(button.getAttribute('data-email'));
    $('#telefoneClienteEdicao').val(button.getAttribute('data-telefone'));
}

function salvarCliente() {
    var nome = document.getElementById('nomeCliente').value;
    var email = document.getElementById('emailCliente').value;
    var telefone = document.getElementById('telefoneCliente').value;

    if (!nome || !telefone) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/prestador/salvarcliente',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nome: nome, email: email, telefone: telefone
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Cliente cadastrado com sucesso!",
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
                        text: "Ocorreu um erro ao cadastrar o cliente.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}

function excluirCliente(button) {
    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir este cliente?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/prestador/excluircliente',
                type: 'POST',
                data: {
                    idCliente: button.getAttribute('data-id')
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
                        case 304:
                            Swal.fire({
                                title: "Ops!",
                                text: "Não foi possível excluir este cliente pois o mesmo possui agendamentos atribuídos à ele.",
                                icon: "warning",
                                confirmButtonText: 'OK'
                            })
                            break;
                        default:
                            Swal.fire({
                                title: "Ops!",
                                text: "Ocorreu um erro ao excluir o cliente.",
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

function salvarEdicaoCliente() {
    var id = document.getElementById('idClienteEdicao').value;
    var nome = document.getElementById('nomeClienteEdicao').value;
    var email = document.getElementById('emailClienteEdicao').value;
    var telefone = document.getElementById('telefoneClienteEdicao').value;

    $.ajax({
        url: '/prestador/salvaredicaocliente',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            nome: nome,
            email: email,
            telefone: telefone
        }),
        complete: function (xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Cliente editado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then(() => {
                        location.reload();
                    });
                    break;
                default:
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao editar o cliente.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }

        }
    })
}

