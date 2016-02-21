/**
 * Created by arash on 13/11/2015.
 */

function loginController($scope, UserService, UserInfo, $location, $window, $state, LOGIN_URI, baseDataService) {
    $scope.message = "";
    /*
    if ($window.sessionStorage.userInfo !=undefined ){
        $window.sessionStorage.userInfo = undefined;
        UserService.setUser('reader', '')
    }
    */
    if (UserInfo.getMessage() != null) {
        $scope.message = UserInfo.getMessage();
    }
    //$scope.userService = UserService;
    $scope.checkUser = function (loginForm) {
        var loginUrl = LOGIN_URI + loginForm.userName + '/' + loginForm.password;
        baseDataService.getBaseData(loginUrl).then(function(response){
            var userInfo = response.data;
            var userLevel;
            if (userInfo != undefined )
            {
                if (userInfo.loginStatus == 1) //login succeeded
                {
                    switch (userInfo.level){

                        case 0:
                            userLevel = 'user';
                            break;
                        case 1:
                            userLevel = 'admin';
                            break;
                        case 2:
                            userLevel = 'admin';
                            break;
                        default :
                            userLevel = 'reader';
                            break;
                    }
                    UserService.setUser(userLevel, loginForm.userName, userInfo.userId);
                    if ($scope.previousState != null) {
                        /*previous page is stored on the route change start function*/
                        if ($scope.previousState.indexOf("logout") > 0 || $scope.previousState.indexOf("login") > 0 )
                        //$location.path('/all/Lift')
                            $state.go('dashboard.listFacility');
                        else
                        //$location.path($scope.previousState);
                            $state.go($scope.previouseState);
                    } else {
                        //$location.path('/all/Lift')
                        $state.go('dashboard.listFacility');
                    }
                } else if (userInfo.loginStatus ==0 ) //login failed
                {
                    UserService.setUser('reader', '', 0);
                    UserInfo.setMessage(userInfo.message);
                    $scope.message = userInfo.message;
                    $state.go('dashboard.login');
                    //$location.path('/login/' + userInfo.message);
                }
            } else
            {
                UserService.setUser('reader', '', 0);
                UserInfo.setMessage('Server is offline');
                $scope.message = userInfo.message;
                $state.go('dashboard.login');
                //$location.path('/login/Server is offline' );
            }
        });
    };
}

