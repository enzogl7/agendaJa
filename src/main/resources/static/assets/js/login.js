document.getElementById('cpfPagante').addEventListener('input', function (e) {
    let value = e.target.value.replace(/\D/g, '');

    value = value.replace(/^(\d{3})(\d)/, '$1.$2');
    value = value.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
    value = value.replace(/\.(\d{3})(\d)/, '.$1-$2');

    e.target.value = value;
});

document.getElementById('cpf').addEventListener('input', function (e) {
    let value = e.target.value.replace(/\D/g, '');

    value = value.replace(/^(\d{3})(\d)/, '$1.$2');
    value = value.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
    value = value.replace(/\.(\d{3})(\d)/, '.$1-$2');

    e.target.value = value;
});

AOS.init();

function modalLogin() {
    $('#modalLogin').modal('show');
}

let etapa = 1;

function mostrarEtapa(n) {
    document.querySelectorAll(".etapa").forEach((e, i) => {
        e.classList.toggle("d-none", i !== n - 1);
    });

    document.getElementById("etapaAtual").innerText = n;
    document.getElementById("btn-voltar").disabled = n === 1;

    const btnAvancar = document.getElementById("btn-avancar");

    if (n === 4) {
        btnAvancar.classList.add("d-none");
        setTimeout(() => {
            const modal = bootstrap.Modal.getInstance(document.getElementById('modalCadastroEtapas'));
        }, 3000);
    } else {
        btnAvancar.classList.remove("d-none");
        btnAvancar.innerText = n === 3 ? "Finalizar" : "Avançar";
    }
}

function avancarEtapa() {
    const etapaAtualDiv = document.querySelector(`#etapa-${etapa}`);
    const inputs = etapaAtualDiv.querySelectorAll("input");
    let camposVazios = false;
    let dataNascimento = null;

    inputs.forEach(input => {
        if (input.type === "checkbox") {
            if (!input.checked) {
                camposVazios = true;
                input.classList.add("is-invalid");
            } else {
                input.classList.remove("is-invalid");
            }
        } else {
            if (!input.value.trim()) {
                camposVazios = true;
                input.classList.add("is-invalid");
            } else {
                input.classList.remove("is-invalid");
            }
        }

        if (input.name === "dataNascimento" || input.id === "dataNascimento") {
            dataNascimento = new Date(input.value);
        }
    });

    if (camposVazios) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios antes de avançar!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    if (dataNascimento) {
        const hoje = new Date();
        const idade = hoje.getFullYear() - dataNascimento.getFullYear();
        const aniversarioEsteAno = new Date(hoje.getFullYear(), dataNascimento.getMonth(), dataNascimento.getDate());

        const menorDeIdade = idade < 18 || (idade === 18 && hoje < aniversarioEsteAno);

        if (menorDeIdade) {
            Swal.fire({
                title: "Ops!",
                text: "Para criar uma conta no AgendaJá, você deve ter pelo menos 18 anos.",
                icon: "error",
                confirmButtonText: "Entendi"
            });
            return;
        }
    }

    const tipoUsuario = document.querySelector('input[name="tipoUsuario"]:checked').value;
    if (etapa === 1 && tipoUsuario === "CLIENTE") {
        etapa = 4;
        cadastrar();
        return;
    }



    if (etapa === 3 && tipoUsuario === "PRESTADOR") {
        etapa = 4;
        cadastrar();
        return;
    }

    if (etapa < 4) {
        etapa++;
        mostrarEtapa(etapa);
    }
}

function voltarEtapa() {
    if (etapa > 1) {
        etapa--;
        mostrarEtapa(etapa);
    }
}

function modalCadastro() {
    etapa = 1;
    mostrarEtapa(etapa);
    const modal = new bootstrap.Modal(document.getElementById('modalCadastroEtapas'));
    modal.show();
}

function cadastrar() {
    var nome = document.getElementById('nome').value;
    var cpf = document.getElementById('cpf').value;
    var email = document.getElementById('email').value;
    var senha = document.getElementById('senha').value;
    var dataNascimento = document.getElementById('dataNascimento').value;
    var tipoUsuario = document.querySelector('input[name="tipoUsuario"]:checked').value;
    var planoSelecionadoInput = document.querySelector('input[name="plano"]:checked');
    // var nomePagante = document.getElementById('nomePagante').value;
    // var cpfPagante = document.getElementById('cpfPagante').value;
    var planoSelecionado = planoSelecionadoInput ? planoSelecionadoInput.value : null;

    $.ajax({
        url: '/register',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nome: nome,
            email: email,
            senha: senha,
            cpf: cpf,
            dataNascimento: dataNascimento,
            planoSelecionado: planoSelecionado,
            userRole: tipoUsuario,
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    mostrarEtapa(4);
                    setTimeout(() => {
                        $('#modalCadastroEtapas').modal('hide');
                        modalLogin();
                    }, 2000);
                    break;
                case 400:
                    Swal.fire({
                        title: "Ops!",
                        text: "Email já cadastrado.",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    })
                    break;
                case 405:
                    Swal.fire({
                        title: "Ops!",
                        text: "CPF já cadastrado.",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    })
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}

function logar() {
    var email = document.getElementById('emailLogin').value;
    var senha = document.getElementById('senhaLogin').value;

    if (!email || !senha) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/logar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            email: email, senha: senha
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    window.location.href = "/prestador/home";
                    break;
                case 202:
                    window.location.href = "/cliente/home";
                    break;
                default:
                    alert("Erro desconhecido: " + status);
                    break;
            }
        }
    });
}

function modalTermos() {
    $('#modalTermos').modal('show');
}

function modalPrivacidade() {
    $('#modalPrivacidade').modal('show');
}
