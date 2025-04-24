$(document).ready(function () {
    paginacaoTabela('tableClientesPrestador');
});


function novoClienteModal() {
    $('#modalNovoCliente').modal('show');
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

