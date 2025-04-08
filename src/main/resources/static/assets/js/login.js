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
            window.location.href = "/home";
        }, 3000);
    } else {
        btnAvancar.classList.remove("d-none");
        btnAvancar.innerText = n === 3 ? "Finalizar" : "Avançar";
    }
}

function avancarEtapa() {
    const etapaAtual = document.querySelector(`#etapa-${etapa}`);
    const inputs = etapaAtual.querySelectorAll("input");
    let camposVazios = false;
    let dataNascimento = null;

    inputs.forEach(input => {
        if (!input.value.trim()) {
            camposVazios = true;
            input.classList.add("is-invalid");
        } else {
            input.classList.remove("is-invalid");
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
                title: "Menor de idade",
                text: "Para criar uma conta no AgendaJá, você deve ter pelo menos 18 anos.",
                icon: "error",
                confirmButtonText: "Entendi"
            });
            return;
        }
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
