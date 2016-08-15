/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('roleCtrl', function($scope, $state,ngDialog, UserService, baseDataService, SUCCESS, FAILURE, ROLE_ADD_URI) {
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

    $scope.appUsers = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'usrName', displayName:'User Name', enableCellEdit:false, width:'20%'},
            {field:'usrFirstName', displayName:'First Name',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.usrFirstName
                }
            },
            {field:'usrSurName',displayName:'Sur Name', enableCellEdit:false , width:'35%',
                cellTooltip: function(row,col) {
                    return row.entity.usrSurName
                }
            },
            {field:'usrActive', displayName:'Active',enableCellEdit:false, width:'5%', cellFilter:'booleanFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) === 'Yes') {
                        return 'green';
                    } else if (grid.getCellValue(row, col) === 'No') {
                        return 'red'
                    }
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeUser(row)"></i></a>', width:'5%' }

        ]
    }
    $scope.appUsers.enableRowSelection = true;
    $scope.appUsers.multiSelect = false;
    $scope.appUsers.noUnselect= true;

    //
    $scope.appUsers.onRegisterApi = function (gridApi) {
        $scope.appUsersGridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
        });
    };



    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.appRole = {};
            $scope.pageIsNew = true;
        } else {
            $scope.pageIsNew = false;
            $scope.appRole = angular.copy(baseDataService.getRow());
            $scope.accessPoints.data = $scope.appRole.accessPoints;
            $scope.appUsers.data = $scope.appRole.appUsers;
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
        }
   }

    //create new user
    $scope.createRole = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        $scope.appRole.accessPoints = $scope.accessPoints.data;
        $scope.appRole.appUsers = $scope.appUsers.data;
        var rowObject = $scope.appRole;
        baseDataService.addRow(rowObject, ROLE_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listRole');
            } else {
                alert('Not able to save role. ' + addResponse.message);
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


    $scope.addUser = function () {
        ngDialog.openConfirm({
            template:'views/pages/userSearch.html',
            controller:'userSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (userList){
                if (userList != undefined) {
                    for (i=0; i<userList.length; i++) {
                        var user = userList[i];
                        $scope.appUsers.data.push(user);
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
                    alert('item is undefined');
                    return;
                }
                row.entity.deleted = true;
                $scope.accessPointsGridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });

    }

    $scope.removeUser = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item?').then(function(result){
            if (result) {

                if (row == undefined || row.entity == undefined) {
                    alert('item is undefined');
                    return;
                }
                row.entity.usrDeleted = true;
                $scope.appUsersGridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });
    }

});
