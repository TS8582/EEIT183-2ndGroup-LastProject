/**
 * 
 */

function alertAndRedirect() {
	Swal.fire({
		title: '請登入會員',
		icon: 'warning',
		confirmButtonText: '確定'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = "http://localhost:8080/PlayCentric/member/login";
		}
	});
}