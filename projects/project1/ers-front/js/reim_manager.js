// Checks if a user is already logged in, if yes redirect to homepage
if (!principal) {
    window.location.href = "./index.html";
}

async function reimbursement() {


    let response = await fetch(`${apiUrl}/reim`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },

    });

    if (response.status == 200) {
        let data = await response.json();
        var list = data;
        document.getElementById("waiting").setAttribute('class', 'd-flex justify-content-center d-none');
        tableReim(list);
    } else if (response.status == 404) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'There was an issue',
        });
    } else if (response.status == 401) {
        Swal.fire({
            icon: 'warning',
            title: 'Oops...',
            text: 'Not Authorized',
        });
    }
}
reimbursement();
function tableReim(list) {

    let x = 1;
    for (i in list) {
        tr = document.createElement('tr');
        createList(x, list[i], tr);
        x++;
        document.getElementById('reim_body').appendChild(tr);
    }
}


function approveButton(id, tr, x) {
    /* <button type="button" class="btn btn-success">Approve</button> */
    let button = document.createElement('button');
    button.setAttribute('class', 'btn btn-success me-1');
    button.innerHTML = "Approve";
    button.addEventListener('click', appoveSendConfirm.bind(null, id, tr, x));
    return button;
}

function denyButton(id, tr, x) {
    /* <button type="button" class="btn btn-danger">Deny</button> */
    let button = document.createElement('button');
    button.setAttribute('class', 'btn btn-danger me-1');
    button.addEventListener('click', denySendConfirm.bind(null, id, tr, x));

    button.innerHTML = "Deny";
    // button.addEventListener('click', test(principal.id));
    return button;
}

function viewButton(id) {
    /* <button type="button" class="btn btn-warning">View</button> */
    let button = document.createElement('button');
    button.setAttribute('class', 'btn btn-warning');
    button.innerHTML = "View";
    button.addEventListener('click', viewSend.bind(null, id));
    return button;
}



function appoveSendConfirm(id, tr, x) {

    Swal.fire({
        title: 'Are you sure?',
        text: "Please confirm if you wish to continue",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes'
    }).then((result) => {
        if (result.isConfirmed) {
            appoveSend(id, tr, x);

        }
    })
}

async function appoveSend(id, tr, x) {
    console.log(principal.id);
    let response = await fetch(`${apiUrl}/reim/${id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: JSON.stringify({
            'user_id': principal.id,
            'status': "approved"
        })
    });

    if (response.status == 200) {
        let data = await response.json();
        let list = data;
        tr.innerHTML = "";
        Swal.fire({
            icon: 'success',
            text: 'Accepted',
        });

        createList(x, list, tr);
    } else if (response.status == 404) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'There was an issue',
        });
    } else if (response.status == 401) {
        Swal.fire({
            icon: 'warning',
            title: 'Oops...',
            text: 'Not Authorized',
        });
    } else if (response.status == 409) {
        Swal.fire({
            icon: 'warning',
            title: 'Oops...',
            text: 'Status has already been changed',
        });
    }
}

function denySendConfirm(id, tr, x) {

    Swal.fire({
        title: 'Are you sure?',
        text: "Please confirm if you wish to continue",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes'
    }).then((result) => {
        if (result.isConfirmed) {
            denySend(id, tr, x);

        }
    })
}

async function denySend(id, tr, x) {

    let response = await fetch(`${apiUrl}/reim/${id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: JSON.stringify({
            'user_id': principal.id,
            'status': "denied"
        })
    });

    if (response.status == 200) {
        let data = await response.json();
        let list = data;
        tr.innerHTML = "";
        Swal.fire({
            icon: 'success',
            text: 'Denied',
        });

        createList(x, list, tr);
    } else if (response.status == 404) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'There was an issue',
        });
    } else if (response.status == 401) {
        Swal.fire({
            icon: 'warning',
            title: 'Oops...',
            text: 'Not Authorized',
        });
    } else if (response.status == 409) {
        Swal.fire({
            icon: 'warning',
            title: 'Oops...',
            text: 'Status has already been changed',
        });
    }
}

async function viewSend(id) {

    let response = await fetch(`${apiUrl}/reim/${id}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });

    if (response.status == 200) {
        let data = await response.json();
        let list = data;
        let single = document.getElementById('view-single');
        let ul = document.createElement('ul');

        let resolved = (list.resolved != null) ? list.resolved.substring(0, 16) : "&nbsp;";
        let resolver = (list.resolver != null) ? list.resolver.username : "&nbsp;"
        let button = document.createElement('button');
        button.setAttribute('class', 'btn btn-info mr-1');
        button.addEventListener('click', viewTable);

        button.innerHTML = "Click to return";

        ul.setAttribute('class', 'list-group list-group-flush');
        single.innerHTML = "";
        ul.innerHTML = `<li class="list-group-item"><span class="btn btn-primary">Amount</span>&nbsp; ${list.amount}</li>` +
            `<li class="list-group-item"><span class="btn btn-primary">Description</span>&nbsp;  ${list.description}</li>` +
            `<li class="list-group-item"><span class="btn btn-primary">Submitted</span>&nbsp;  ${list.submitted.substring(0, 16)}</li>` +
            `<li class="list-group-item"><span class="btn btn-primary">Resolved</span>&nbsp;  ${resolved}</li>` +
            `<li class="list-group-item"><span class="btn btn-primary">Resolver</span>&nbsp;  ${resolver}</li>` +
            `<li class="list-group-item"><span class="btn btn-primary">Status</span>&nbsp;  ${list.reim_status.reimb_status}</li>` +
            `<li class="list-group-item"><span class="btn btn-primary">Type</span>&nbsp;  ${list.reim_type.reimb_type}</li>`;
        single.append(button);

        single.append(ul);
        document.getElementById("main-table").style.display = "none";
        document.getElementById("view-single").style.display = "block";
    } else if (response.status == 404) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'There was an issue',
        });
    } else if (response.status == 401) {
        Swal.fire({
            icon: 'warning',
            title: 'Oops...',
            text: 'Not Authorized',
        });
    }
}

function viewTable() {
    document.getElementById("main-table").style.display = "block";
    document.getElementById("view-single").style.display = "none";
}

function createList(x, list, tr) {
    td = createTableData(x);
    tr.appendChild(td);
    td = createTableData(list.amount);
    tr.appendChild(td);
    td = createTableData(list.description);
    tr.appendChild(td);
    td = createTableData(list.submitted.substring(0, 16));
    tr.appendChild(td);
    td = createTableData((list.resolved != null) ? list.resolved.substring(0, 16) : "&nbsp;");
    tr.appendChild(td);
    td = createTableData((list.resolver != null) ? list.resolver.username : "&nbsp;");
    tr.appendChild(td);
    td = createTableData(list.reim_status.reimb_status);
    tr.appendChild(td);
    td = createTableData(list.reim_type.reimb_type);
    tr.appendChild(td);
    td = document.createElement('td');
    if (list.reim_status.reimb_status == 'pending') {
        td.appendChild(approveButton(list.id, tr, x));
        td.appendChild(denyButton(list.id, tr, x));
    }
    td.appendChild(viewButton(list.id));
    td.style.width = '20%';
    tr.appendChild(td);
}