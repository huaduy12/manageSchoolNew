jQuery(".form-valide").validate({
    ignore: [],
    errorClass: "invalid-feedback animated fadeInDown",
    errorElement: "div",
    errorPlacement: function (e, a) {
        jQuery(a).parents(".form-group > div").append(e);
    },
    highlight: function (e) {
        jQuery(e)
            .closest(".form-group")
            .removeClass("is-invalid")
            .addClass("is-invalid");
    },
    success: function (e) {
        jQuery(e).closest(".form-group").removeClass("is-invalid"),
            jQuery(e).remove();
    },
    rules: {
        "val-username": { required: !0, minlength: 5 },
        "val-usernameMother": { required: !0, minlength: 5 },
        "val-email": { required: !0, email: !0 },
        "val-password": { required: !0, minlength: 5 },
        "val-confirm-password": { required: !0, equalTo: "#val-password" },
        "val-select2": { required: !0 },
        "val-select2-multiple": { required: !0, minlength: 2 },
        "val-suggestions": { required: !0, minlength: 5 },
        "val-skill": { required: !0 },
        "val-currency": { required: !0, currency: ["$", !0] },
        "val-website": { required: !0, url: !0 },
        "val-digits": { required: !0, digits: !0 },
        "val-address": { required: !0, minlength:10 },
        "val-addressMother": { required: !0, minlength:10 },
        "val-number": { required: !0, number: !0 },
        "val-range": { required: !0, range: [1, 5] },
        "val-terms": { required: !0 },
    },
    messages: {
        "val-username": {
            required: "Vui lòng nhập họ và tên",
            minlength: "Nhập tối thiếu 5 ký tự",
        },
        "val-usernameMother": {
            required: "Vui lòng nhập họ và tên",
            minlength: "Nhập tối thiếu 5 ký tự",
        },
        "val-email": "Please enter a valid email address",
        "val-password": {
            required: "Please provide a password",
            minlength: "Your password must be at least 5 characters long",
        },
        "val-confirm-password": {
            required: "Please provide a password",
            minlength: "Your password must be at least 5 characters long",
            equalTo: "Please enter the same password as above",
        },
        "val-select2": "Please select a value!",
        "val-select2-multiple": "Please select at least 2 values!",
        "val-suggestions": "What can we do to become better?",
        "val-skill": "Please select a skill!",
        "val-currency": "Please enter a price!",
        "val-website": "Please enter your website!",
        "val-phoneus": "Vui lòng nhập số điện thoại",
        "val-digits": "Please enter only digits!",
        "val-address": {
            required: "Vui lòng nhập địa chỉ",
            minlength: "Nhập tối thiếu 10 ký tự",
        },
        "val-addressMother": {
            required: "Vui lòng nhập địa chỉ",
            minlength: "Nhập tối thiếu 10 ký tự",
        },
        "val-number": "Please enter a number!",
        "val-range": "Please enter a number between 1 and 5!",
        "val-terms": "You must agree to the service terms!",
    },
});
