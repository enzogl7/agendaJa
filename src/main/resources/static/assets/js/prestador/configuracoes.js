function adicionarFolga() {
    const inputData = document.getElementById("dataFolga");
    const lista = document.getElementById("listaFolgas");
    const data = inputData.value;

    if (!data) return;

    const item = document.createElement("li");
    item.className = "list-group-item d-flex justify-content-between align-items-center text-[#7F8C8D]";
    item.innerHTML = `${data} <button class="btn btn-sm btn-outline-danger" onclick="this.parentElement.remove()">Remover</button>`;
    lista.appendChild(item);

    inputData.value = "";
}