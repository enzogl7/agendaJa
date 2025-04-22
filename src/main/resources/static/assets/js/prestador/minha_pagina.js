var telefoneInput = document.getElementById('telefoneNegocio');
IMask(telefoneInput, {
    mask: '(00) 00000-0000'
});

document.addEventListener("DOMContentLoaded", function () {
    const btnSalvar = document.getElementById("btnSalvar");
    const formInputs = document.querySelectorAll("input, textarea, select");
    const btnCompartilhar = document.getElementById('btnCompartilhar');
    const copiadoMsg = document.getElementById('copiadoMsg');

    formInputs.forEach(input => {
        input.addEventListener("input", () => {
            btnSalvar.disabled = false;
        });
    });

    $('#servicoNegocio').on('change', function () {
        btnSalvar.disabled = false;
    });

    if (btnCompartilhar) {
        btnCompartilhar.addEventListener('click', function () {
            const slug = document.getElementById('paginaSlug').value;
            if (!slug) return;

            const url = `${window.location.origin}/negocio/${slug}`;

            navigator.clipboard.writeText(url).then(() => {
                copiadoMsg.classList.remove('hidden');

                setTimeout(() => {
                    copiadoMsg.classList.add('hidden');
                }, 2000);
            }).catch(() => {
                alert("Erro ao copiar o link.");
            });
        });
    }
});

$(document).ready(function() {
    $('#servicoNegocio').select2({
        placeholder: 'Selecione um ou mais serviços',
        width: '100%',
        allowClear: true
    });
});

document.getElementById("instagramNegocio").addEventListener("blur", function() {
    validarLinkInstagram();
});



function salvarMinhaPagina() {
    var nome = document.getElementById('nomeNegocio').value;
    var descricao = document.getElementById('descricaoNegocio').value;
    var cidade = document.getElementById('cidadeNegocio').value;
    var endereco = document.getElementById('enderecoNegocio').value;
    var servicos = $('#servicoNegocio').val();
    var email = document.getElementById('emailNegocio').value;
    var telefone = document.getElementById('telefoneNegocio').value;
    var instagram = document.getElementById('instagramNegocio').value;

    if (!nome || !descricao || !cidade || !endereco || !servicos || !telefone) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    validarLinkInstagram();

    $.ajax({
        url: '/prestador/salvarpagina',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nome: nome, descricao: descricao, cidade: cidade, endereco: endereco, servicos: servicos, email: email, telefone: telefone, instagram: instagram
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Página atualizada com sucesso!",
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
                        text: "Ocorreu um erro ao salvar a página.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}

function validarLinkInstagram() {
    var input = document.getElementById("instagramNegocio");
    var link = input.value.trim();

    if (link) {
        if (!link.includes("instagram.com")) {
            Swal.fire({
                title: "Ops!",
                text: "Insira um link válido do Instagram.",
                icon: "warning",
                confirmButtonText: "Ok"
            }).then((result) => {
                if (result.isConfirmed) {
                    input.value = "";
                    input.focus();
                    return;
                }
            });
            return;
        }
    }

    if (link && !link.startsWith("https://") && !link.startsWith("http://")) {
        if (link.startsWith("instagram.com")) {
            link = "https://" + link;
        } else {
            link = "https://instagram.com/" + link;
        }
        input.value = link;
    }

}
