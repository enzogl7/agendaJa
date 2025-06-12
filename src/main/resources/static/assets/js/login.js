document.getElementById('cpf').addEventListener('input', function (e) {
    let value = e.target.value.replace(/\D/g, '');

    value = value.replace(/^(\d{3})(\d)/, '$1.$2');
    value = value.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
    value = value.replace(/\.(\d{3})(\d)/, '.$1-$2');

    e.target.value = value;
});

AOS.init();

document.addEventListener("DOMContentLoaded", function () {
    const clienteRadio = document.getElementById("cliente");
    const prestadorRadio = document.getElementById("prestador");
    const campoCpf = document.getElementById("campoCpf");
    const campoDataNascimento = document.getElementById("campoDataNascimento");
    const inputCpf = document.getElementById("cpf");
    const inputDataNascimento = document.getElementById("dataNascimento");

    function atualizarCamposVisiveis() {
        const isCliente = clienteRadio.checked;
        campoCpf.style.display = isCliente ? "none" : "block";
        campoDataNascimento.style.display = isCliente ? "none" : "block";

        inputCpf.required = !isCliente;
        inputDataNascimento.required = !isCliente;
    }

    clienteRadio.addEventListener("change", atualizarCamposVisiveis);
    prestadorRadio.addEventListener("change", atualizarCamposVisiveis);

    atualizarCamposVisiveis();

    const urlParams = new URLSearchParams(window.location.search);
    const pagamentoStatus = urlParams.get("pagamento");
    if (pagamentoStatus) {
        modalLoginPagamento(pagamentoStatus)
    }
});


function modalLogin() {
    $('#modalLogin').modal('show');
}

function modalLoginPagamento(status) {
    $('#modalLogin').modal('show');
    if (status === 'sucesso') {
        const mensagem = document.getElementById("mensagemPagamentoSucesso");
        mensagem.classList.remove("hidden");
    }
    else if (status === 'erro') {
        const mensagem = document.getElementById("mensagemPagamentoErro");
        mensagem.classList.remove("hidden");
    }
}

let etapa = 1;

function mostrarEtapa(n) {
    document.querySelectorAll(".etapa").forEach((e, i) => {
        e.classList.toggle("d-none", i !== n - 1);
    });

    document.getElementById("etapaAtual").innerText = n;
    document.getElementById("btn-voltar").disabled = n === 1;

    const btnAvancar = document.getElementById("btn-avancar");
    const btnVoltar = document.getElementById("btn-voltar");

    if (n === 4) {
        btnAvancar.classList.add("d-none");
        btnVoltar.classList.add("d-none");
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
        if (input.closest('div').style.display === 'none') return;

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

        if ((input.name === "dataNascimento" || input.id === "dataNascimento") && input.value) {
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

    const planoSelecionado = document.querySelector('input[name="plano"]:checked');
    if (etapa === 2) {
        if (!planoSelecionado) {
            Swal.fire({
                title: "Ops!",
                text: "Por favor, escolha um plano antes de continuar.",
                icon: "warning",
                confirmButtonText: "OK"
            });
            return;
        }

        if (planoSelecionado.value === "BASICO") {
            etapa = 4;
            cadastrar();
            return;
        }
    }

    if (etapa === 2 && planoSelecionado.value !== "BASICO") {
        cadastrar(true);
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

function cadastrar(preRegistro = false) {
    var nome = document.getElementById('nome').value;
    var cpf = document.getElementById('cpf').value;
    var email = document.getElementById('email').value;
    var senha = document.getElementById('senha').value;
    var dataNascimento = document.getElementById('dataNascimento').value;
    var tipoUsuario = document.querySelector('input[name="tipoUsuario"]:checked').value;
    var planoSelecionadoInput = document.querySelector('input[name="plano"]:checked');
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
            preRegistro: preRegistro
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    if (preRegistro) {
                        break;
                    }
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
                case 401:
                    Swal.fire({
                        title: "Ops!",
                        text: "Email ou senha incorretos!",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    })
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

function validarSenha() {
    const senha = document.getElementById("senha").value;
    const temMaiuscula = /[A-Z]/.test(senha);
    const temNumero = /\d/.test(senha);
    const temEspecial = /[!@#$%^&*(),.?":{}|<>]/.test(senha);
    const temMinimo = senha.length >= 8;

    atualizarRequisito("req-maiuscula", temMaiuscula, "Ao menos 1 letra maiúscula");
    atualizarRequisito("req-numero", temNumero, "Ao menos 1 número");
    atualizarRequisito("req-especial", temEspecial, "Ao menos 1 caractere especial");
    atualizarRequisito("req-minimo", temMinimo, "Pelo menos 8 caracteres");
    document.getElementById("btn-avancar").disabled = !(temMaiuscula && temNumero && temEspecial && temMinimo);
}

function atualizarRequisito(id, valido, texto) {
    const el = document.getElementById(id);
    const icone = valido ? '<i class="bi bi-check"></i>' : '<i class="bi bi-x"></i>';
    el.innerHTML = `${icone} ${texto}`;
    el.classList.remove("text-success", "text-danger", "text-muted");
    el.classList.add(valido ? "text-success" : "text-danger");
}

function pagarStripe() {
    const button = document.getElementById("btnStripeCheckout");
    const text = document.getElementById("btnStripeText");
    const spinner = document.getElementById("btnStripeSpinner");
    const email = document.getElementById('email').value;

    button.disabled = true;
    text.textContent = "Redirecionando...";
    spinner.classList.remove("hidden");

    $.ajax({
        url: '/product/v1/checkout',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            priceId: "price_1RZAHcKFFRANfNose1KR7sHr",
            quantity: "1",
            email: email
        }),
        success: function(response) {
            if (response && response.sessionUrl) {
                window.open(response.sessionUrl, '_blank');
            } else {
                Swal.fire("Erro", "URL de pagamento não encontrada.", "error");
            }
        },
        error: function(xhr) {
            console.error("Erro ao redirecionar:", xhr);
            Swal.fire("Erro", "Falha ao iniciar o pagamento.", "error");
        },
        complete: function() {
            button.disabled = false;
            text.textContent = "Ir para Stripe";
            spinner.classList.add("hidden");
        }
    });
}
