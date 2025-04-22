document.getElementById("precoServico").addEventListener("input", function (e) {
    let value = e.target.value;
    value = value.replace(/\D/g, "");
    value = (Number(value) / 100).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL",
    });
    e.target.value = value;
});

document.getElementById("precoServicoEdicao").addEventListener("input", function (e) {
    let value = e.target.value;
    value = value.replace(/\D/g, "");
    value = (Number(value) / 100).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL",
    });
    e.target.value = value;
});

function novoServicoModal() {
    $('#modalNovoServico').modal('show');
}

function salvarServico() {
    var nome = document.getElementById('nomeServico').value;
    var descricao = document.getElementById('descricaoServico').value;
    var preco = document.getElementById('precoServico').value;
    var categoria = document.getElementById('categoriaServico').value;

    if (!nome || !preco || !categoria) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/prestador/salvarservico',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nome: nome, descricao: descricao, preco: preco, categoria: categoria
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Serviço criado com sucesso!",
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
                        text: "Ocorreu um erro ao salvar o serviço.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}

function excluirServico(button) {
    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir este serviço?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/prestador/excluirservico',
                type: 'POST',
                data: {
                    idServico: button.getAttribute('data-id')
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
                                text: "Não foi possível excluir este serviço pois o mesmo possui agendamentos atribuídos à ele.",
                                icon: "warning",
                                confirmButtonText: 'OK'
                            })
                            break;
                        default:
                            Swal.fire({
                                title: "Ops!",
                                text: "Ocorreu um erro ao excluir o serviço.",
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

function modalEditarServico(button) {
    $('#modalEditarServico').modal('show');
    $('#idServicoEdicao').val(button.getAttribute('data-id'));
    $('#nomeServicoEdicao').val(button.getAttribute('data-nome'));
    $('#descricaoServicoEdicao').val(button.getAttribute('data-descricao'));
    $('#precoServicoEdicao').val(button.getAttribute('data-preco'));
    $('#categoriaServicoEdicao').val(button.getAttribute('data-categoria'));
    const ativo = button.getAttribute('data-ativo') === 'true';
    $('#ativoServicoEdicao').prop('checked', ativo);
}

function salvarEdicaoServico() {
    var id = document.getElementById('idServicoEdicao').value;
    var nome = document.getElementById('nomeServicoEdicao').value;
    var descricao = document.getElementById('descricaoServicoEdicao').value;
    var preco = document.getElementById('precoServicoEdicao').value;
    var categoria = document.getElementById('categoriaServicoEdicao').value;
    var ativo = document.getElementById('ativoServicoEdicao').checked;

    if (!nome || !preco || !categoria) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/prestador/editarservico',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
           id: id, nome: nome, descricao: descricao, preco: preco, categoria: categoria, ativo: ativo
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Serviço editado com sucesso!",
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
                        text: "Ocorreu um erro ao editar o serviço.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}