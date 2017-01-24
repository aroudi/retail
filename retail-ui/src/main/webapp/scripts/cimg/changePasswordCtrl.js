/**
 * Created by arash on 13/11/2015.
 */

cimgApp.controller('changePasswordCtrl', function($scope, UserService, $state, CHANGE_PASSWORD_URI, baseDataService) {
    $scope.message = "";
    /*
    if ($window.sessionStorage.userInfo !=undefined ){
        $window.sessionStorage.userInfo = undefined;
        UserService.setUser('reader', '')
    }
    */
    $scope.message = '';
    //$scope.userService = UserService;
    var userId = UserService.getUser().id;
    if (userId == undefined || userId == 0) {
        alert('you need to login first');
        $state.go('dashboard.login');
    }
    $scope.userName = UserService.getUser().usrName;

    $scope.changePassword = function () {
        if ($scope.retryPassword != $scope.changePassForm.password) {
            $scope.message = 'Password and Retry Password dose not match!!!';
            return
        }
        var userId = UserService.getUser().id;
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        $scope.changePassForm.userId = userId;
        baseDataService.addRow($scope.changePassForm, CHANGE_PASSWORD_URI).then(function(response) {
            var addResponse = response.data;
            $scope.message = addResponse.message;
        });
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }
});

