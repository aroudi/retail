/**
 * Created by arash on 14/08/2015.
 */
function viewMessageBodyCtrl( $scope, $state,UserService, baseDataService, SUCCESS, MESSAGEBODY_UPDATE_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        $scope.messageChannel = angular.copy(baseDataService.getRow());
        baseDataService.setRow({});
    }
    //create new facility
    $scope.updateMessageBody = function () {
        userId = UserService.getUserId();
        if (userId == undefined) {
            alert('you need to login first');
            $state.go($scope.login);
        }
        $scope.messageChannel.userId = userId;
        var rowObject = $scope.messageChannel;
        baseDataService.addRow(rowObject, MESSAGEBODY_UPDATE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listIncident');
            } else {
                alert('Not able to update message body. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    };
}
