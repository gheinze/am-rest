

// Bootstrap

$(document).ready(function () {
    appModel.init();
});


// Model definition

var appModel = {};


// Init
(function(exports) {

    exports.init = function() {
        exports.logout();
    };

})(appModel);



// User Controller

(function(exports) {

    exports.identity = "";
    var loggedIn = false;


    exports.jwtEcho = function () {
        $.ajax({
            url: '/api/jwt-echo/',
            type: 'GET',
            beforeSend: function(xhr) {
                xhr.setRequestHeader('Authorization', (null === exports.identity) ? null : exports.identity.authorizationHeader);
            },
            data: {},
            success: function(data) {
                alert(JSON.stringify(data));
            },
            error: function(jqXHR, exception) {
                alert("Fail: " + jqXHR.responseText || exception);
            }
        });
    };


    exports.login = function(provider) {
        var win = window.open('/login/' + provider, "Login Window", '_blank', 'toolbar=0,location=0,menubar=0');
        $(win).on('load', function() {
            var responseText = win.document.documentElement.innerText || win.document.documentElement.textContent;
            var identity = JSON.parse(responseText);
            win.opener.appModel.setIdentity(identity);
            win.close();
        });
    };

    exports.logout = function() {
        exports.identity = null;
        $(".authenticatedState").hide();
        $(".nonAuthenticatedState").show();
        $("#userAvatar").empty();
        $("#userNameId").empty();
        $("#jwtId").empty();

        $("#restCallLabelId").empty();
        var span = document.createElement("span");
        span.textContent = "Make an unauthenticated REST call:";
        $("#restCallLabelId").append(span);

    };



    exports.setIdentity = function(identity) {

        exports.identity = identity;

        var image = document.createElement('img');
        image.setAttribute('src', identity.userInfo.imageUrl);
        image.className = 'avatar-img';
        $("#userAvatar").append(image);

        var link = document.createElement('a');
        link.setAttribute('href', 'mailto:' + identity.userInfo.email);
        link.innerHTML = identity.userInfo.name;
        $("#userNameId").append(link);

        $("#jwtId").html(identity.authorizationHeader);

        $("#restCallLabelId").empty();
        var span = document.createElement("span");
        span.textContent = "Make an authenticated REST call:";
        $("#restCallLabelId").append(span);

        $(".authenticatedState").show();
        $(".nonAuthenticatedState").hide();
    };



})(appModel);
