function confirmDelete(obj) {
    Swal.fire({
        text: '確認刪除？',
        icon: 'warning',
        showCancelButton: true,
        buttonsStyling: false,
        customClass: {
            confirmButton: 'btn btn-primary mx-2 px-4',
            cancelButton: 'btn btn-danger mx-2 px-4',
        },
        confirmButtonText: '確認',
        cancelButtonText: '取消'
    }).then((result) => {
        if (result.value) {
            window.location.href = obj.href;
        }
    })
    return false;
}

function confirmUserDisable(obj) {
    Swal.fire({
        text: '確認停權？',
        icon: 'warning',
        showCancelButton: true,
        buttonsStyling: false,
        customClass: {
            confirmButton: 'btn btn-primary mx-2 px-4',
            cancelButton: 'btn btn-danger mx-2 px-4',
        },
        confirmButtonText: '確認',
        cancelButtonText: '取消'
    }).then((result) => {
        if (result.value) {
            window.location.href = obj.href;
        }
    })
    return false;
}