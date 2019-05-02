/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('userCtrl', function($scope, $state,$stateParams,ngDialog, UserService, baseDataService, SUCCESS, FAILURE, USER_ADD_URI) {
    //set default data on the page
    $scope.accessPoints = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'acptToken', visible:false,enableCellEdit:false, enableCellEdit:false},
            {field:'acptName', displayName:'Access Point',enableCellEdit:false, width:'45%',
                cellTooltip: function(row,col) {
                    return row.entity.acptName
                }
            },
            {field:'acptDesc', displayName:'Description',enableCellEdit:false, width:'45%',
                cellTooltip: function(row,col) {
                    return row.entity.acptDesc
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeAccessPoint(row)"></i></a>', width:'5%' }
        ]
    }
    $scope.accessPoints.enableRowSelection = true;
    $scope.accessPoints.multiSelect = false;
    $scope.accessPoints.noUnselect= true;

    //
    $scope.accessPoints.onRegisterApi = function (gridApi) {
        $scope.accessPointsGridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
        });
    };

    $scope.appRoles = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'roleCode', displayName:'Code',enableCellEdit:false, width:'30%'},
            {field:'roleName', displayName:'Role Name',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.roleName
                }
            },
            {field:'roleDesc', displayName:'Description',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.roleDesc
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeAccessPoint(row)"></i></a>', width:'5%' }
        ]
    }
    $scope.appRoles.enableRowSelection = true;
    $scope.appRoles.multiSelect = false;
    $scope.appRoles.noUnselect= true;

    //
    $scope.appRoles.onRegisterApi = function (gridApi) {
        $scope.appRolesGridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
        });
    };


    initPageData();
    function initPageData() {
        if ( $stateParams.blankPage) {
            $scope.appUser = {};
            $scope.pageIsNew = true;
            $scope.retryPassword='';
        } else {
            $scope.pageIsNew = false;
            $scope.appUser = angular.copy(baseDataService.getRow());
            $scope.accessPoints.data = $scope.appUser.accessPoints;
            $scope.appRoles.data = $scope.appUser.appRoles;
            //baseDataService.setIsPageNew(true);
            //baseDataService.setRow({});
        }
   }

    //create new user
    $scope.createUser = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        /*
        if ($scope.pageIsNew) {
            if ($scope.retryPassword != $scope.appUser.usrPass) {
                baseDataService.displayMessage('info','Warning', 'Password and Retry does not match ');
                return
            }
        }
        */
        $scope.appUser.accessPoints = $scope.accessPoints.data;
        $scope.appUser.appRoles = $scope.appRoles.data;
        var rowObject = $scope.appUser;
        baseDataService.addRow(rowObject, USER_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listUser');
            } else {
                baseDataService.displayMessage('info','Warning', 'Not able to save user. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

    $scope.addAccessPoint = function () {
        ngDialog.openConfirm({
            template:'views/pages/accessPointSearch.html',
            controller:'accessPointSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (accessPointList){
                if (accessPointList != undefined) {
                    for (i=0; i<accessPointList.length; i++) {
                        var accessPoint = accessPointList[i];
                        $scope.accessPoints.data.push(accessPoint);
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }

    $scope.addRole = function () {
        ngDialog.openConfirm({
            template:'views/pages/roleSearch.html',
            controller:'roleSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (roleList){
                if (roleList != undefined) {
                    for (i=0; i<roleList.length; i++) {
                        var role = roleList[i];
                        $scope.appRoles.data.push(role);
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }

    $scope.removeAccessPoint = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this access point?').then(function(result){
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    baseDataService.displayMessage('info','Warning', 'item is undefined!!!');
                    return;
                }
                row.entity.deleted = true;
                $scope.accessPointsGridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });
    }

    $scope.removeRole = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this role?').then(function(result){
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    baseDataService.displayMessage('info','Warning', 'item is undefined!!!');
                    return;
                }
                row.entity.roleDeleted = true;
                $scope.appRolesGridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });
    }

});
