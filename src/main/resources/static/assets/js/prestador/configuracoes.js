let datasFolga = [];
const tipoChaveSelect = document.getElementById('tipoChavePix');
const chavePixInput = document.getElementById('chavePix');

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.folga-salva').forEach(item => {
        const data = item.textContent.trim();
        if (data) {
            datasFolga.push(data);
        }
    });
    atualizarListaFolgas();
});

window.onload = function() {
    const tipoChaveSalvo = localStorage.getItem('tipoChavePix');
    if (tipoChaveSalvo) {
        tipoChaveSelect.value = tipoChaveSalvo;
        atualizarCampoChave(tipoChaveSalvo);
    }
};

tipoChaveSelect.addEventListener('change', function() {
    chavePixInput.value = ""
    localStorage.setItem('tipoChavePix', tipoChaveSelect.value);
    atualizarCampoChave(tipoChaveSelect.value);
});

function adicionarFolga() {
    const inputFolga = document.getElementById('dataFolga');
    const data = inputFolga.value;

    if (!data || datasFolga.includes(data)) {
        return;
    }

    datasFolga.push(data);
    atualizarListaFolgas();
    inputFolga.value = '';
}

function atualizarListaFolgas() {
    const lista = document.getElementById('listaFolgas');
    lista.innerHTML = '';

    datasFolga.forEach((data, index) => {
        if (data.includes('-')) {
            const [ano, mes, dia] = data.split('-');
            data = `${dia}/${mes}/${ano}`;
        }

        const li = document.createElement('li');
        li.className = 'list-group-item d-flex justify-content-between align-items-center';
        li.innerHTML = `
        ${data}
        <button class="btn btn-sm btn-outline-danger" onclick="removerFolga(${index})">
            <i class="bi bi-x"></i>
        </button>
    `;
        lista.appendChild(li);
    });
}

function removerFolga(index) {
    datasFolga.splice(index, 1);
    atualizarListaFolgas();
}

function salvarHorarios() {
    const inicio_expediente = document.getElementById('inicioExpediente').value;
    const fim_expediente = document.getElementById('fimExpediente').value;
    const inicio_pausa = document.getElementById('inicioPausa').value;
    const fim_pausa = document.getElementById('fimPausa').value;

    if (!inicio_expediente || !fim_expediente) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha os campos de expediente!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/prestador/salvarhorarios',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            inicio_expediente,
            fim_expediente,
            inicio_pausa,
            fim_pausa,
            datasFolga
        }),
        complete: function (xhr) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Horários de trabalho ajustados com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then(result => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    });
                    break;
                default:
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao salvar os horários.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
            }
        }
    });
}

function verificarInputData() {
    const input = document.getElementById('dataFolga');
    const botao = document.getElementById('btnAdicionarFolga');
    botao.disabled = !input.value;
}

function salvarChavePix() {
    const chavePix = document.getElementById('chavePix').value;

    $.ajax({
        url: '/prestador/salvarpix',
        type: 'POST',
        data: {
            chavePix: chavePix
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "PIX registrado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then(() => {
                        location.reload();
                    });
                    break;
                default:
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao registrar a chave PIX.",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                    return;
            }
        }
    });
}

function formatarTelefone(telefone) {
    telefone = telefone.replace(/\D/g, '');

    if (telefone.length <= 11) {
        return '+55 ' + telefone.replace(/(\d{2})(\d{5})(\d{4})/, '$1 $2-$3');
    } else {
        return telefone;
    }
}

function cpfInputListener(e) {
    let value = e.target.value.replace(/\D/g, '');

    value = value.replace(/^(\d{3})(\d)/, '$1.$2');
    value = value.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
    value = value.replace(/\.(\d{3})(\d)/, '.$1-$2');

    e.target.value = value;
}

function atualizarCampoChave(tipoChave) {
    chavePixInput.removeEventListener('input', cpfInputListener);
    chavePixInput.maxLength = 40;

    if (tipoChave === 'telefone') {
        chavePixInput.type = 'tel';
        chavePixInput.value = formatarTelefone(chavePixInput.value);
        chavePixInput.placeholder = 'Exemplo: +55 11 91234-5678';
    } else if (tipoChave === 'email') {
        chavePixInput.type = 'email';
        chavePixInput.placeholder = 'Exemplo: email@dominio.com';
    } else if (tipoChave === 'cpf') {
        chavePixInput.type = 'text';
        chavePixInput.placeholder = '123.456.789-10';
        chavePixInput.maxLength = 14;
        chavePixInput.addEventListener('input', cpfInputListener);
    } else {
        chavePixInput.type = 'text';
        chavePixInput.placeholder = 'Digite sua chave';
    }
}

