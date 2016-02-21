/**
 * Created by arash on 14/08/2015.
 */
function incidentListCtrl($scope, $state, baseDataService, incidentService , SUCCESS, FAILURE, INCIDENT_ALL_URI, INCIDENT_GET_URI, STATION_MSG_GET_URI, TRACKWORK_GET_URI, MESSAGEBODY_VIEW_URI, MESSAGEBODY_UPDATE_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        expandableRowTemplate: 'views/pages/incidentChannelsTemplate.html',
        expandableRowHeight: 300,
        enableRowSelection :true,
        expandableRowScope:{
                publishMessage : function(row) {
                var parentRow = row.grid.appScope.row;
                var incident = parentRow.entity;
                if (row == undefined || row.entity == undefined) {
                    alert('row is undefined');
                    return;
                }
                var message = row.entity;
                if (message.channel.name == 'TLSNB') {
                    //set the row to retrieve message title on the page
                    var tlsNbForm = {};
                    tlsNbForm.incidentId = incident.notificationId;
                    tlsNbForm.name = incident.heading;
                    tlsNbForm.iimsNumber = incident.iimsNumber;
                    tlsNbForm.messageBody = message.messageBody;
                    tlsNbForm.messageBodyId = message.id;
                    tlsNbForm.dvaText = message.dvaText;
                    baseDataService.setRow(tlsNbForm);
                    //redirect to the incident page.
                    $state.go('dashboard.tlsNBPublish');
                }
            },
            subGridVariable: 'subGridScopeVariable'
        } ,
        columnDefs: [
            {field:'notificationId', visible:false, enableCellEdit:false},
            {field:'messageType.name', displayName:'Message Type',enableCellEdit:false, width:'10%'},
            {field:'heading', displayName:'Message Title',enableCellEdit:false, width:'40%'},
            {field: 'status.name', displayName: 'Status', enableCellEdit: false, width:'5%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) === 'Pending') {
                        return 'amber';
                    } else if (grid.getCellValue(row, col) === 'Active') {
                        return 'green'
                    } else if (grid.getCellValue(row, col) === 'In-Active') {
                        return 'red'
                    }
                }
            },
            {field:'lastModifiedDate', displayName:'Modified On', displayName:'Last Modified On',enableCellEdit:false, cellFilter:'date:\'yyyy-MM-dd HH:mm\'', width:'10%'},
            {field:'lastModifiedBy',  displayName:'Modified By',cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'iimsNumber', displayName:'IIMS Number',enableCellEdit:false, width:'10%'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editIncident(row)"></i></a>&nbsp;<a href=""><i tooltip="Preview Message Body" tooltip-placement="bottom" class="fa fa-envelope fa-2x" ng-click="grid.appScope.editMessageBody(row)"></i></a>&nbsp;<a href=""><i tooltip="Delete" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ></i></a>'}
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        /*
        gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
        });
        */
    };
    getAllIncidents();

    function getAllIncidents() {
        baseDataService.getBaseData(INCIDENT_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            for (i=0; i<data.length; i++) {
                data[i].subGridOptions = {
                    columnDefs :[
                                    {name:"id", field:"id", visible:false},
                                    {name:"notificationId",field:"notificationId" , visible:false},
                                    {name:"channel", field:"channel.name", width:'5%'},
                                    {name: "status", field: "status.name", width: '5%',
                                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                                            if (grid.getCellValue(row, col) === 'Off-Line') {
                                                return 'amber';
                                            } else if (grid.getCellValue(row, col) === 'On-Line') {
                                                return 'green'
                                            } else if (grid.getCellValue(row, col) === 'In-Active') {
                                                return 'red'
                                            }
                                        }
                                    },
                                    {name:'lastModifiedDate', field:'lastModifiedDate',  displayName:'Modified On',width:'8%',enableCellEdit:false, cellFilter:'date:\'yyyy-MM-dd HH:mm\'' },
                                    {name:'lastModifiedBy',  displayName:'Modified By',field:'lastModifiedBy', cellFilter:'fullName', width:'7%',enableCellEdit:false},
                                    {name:"messageTitle", field:"messageTitle" , width:'15%'},
                                    {name:"messageBody", field:"messageBody" , width:'50%'},
                                    {name:'Action', width:'10%',cellTemplate:'<button type="button" class="btn btn-success" ng-click="grid.appScope.publishMessage(row)" >Submit</button>'}

                    ],
                    data:data[i].messageChannels
                }
            }
            $scope.gridOptions.data = data;
        });
    }

    $scope.editIncident = function(row) {
        // fetch the incident details from the server and save it on the service layer.
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var message = row.entity;
        if (message.messageType.name == 'Incident') {
            var incidentGetURI = INCIDENT_GET_URI + '/' + row.entity.notificationId;
            baseDataService.getBaseData(incidentGetURI).then(function(response){
                baseDataService.setIsPageNew(false);
                baseDataService.setRow(response.data);
                //redirect to the incident page.
                $state.go('dashboard.createIncident');
            });
        } else if (message.messageType.name =='Station') {
            var stationMessageGetURI = STATION_MSG_GET_URI + row.entity.notificationId;
            baseDataService.getBaseData(stationMessageGetURI).then(function(response){
                baseDataService.setIsPageNew(false);
                baseDataService.setRow(response.data);
                //redirect to the incident page.
                $state.go('dashboard.createStationMessage');
            });
        } else {
            var trackworkGetURI = TRACKWORK_GET_URI + row.entity.notificationId;
            baseDataService.getBaseData(trackworkGetURI).then(function(response){
                baseDataService.setIsPageNew(false);
                baseDataService.setRow(response.data);
                //redirect to the incident page.
                $state.go('dashboard.createTrackwork');
            });
        }

    }
    $scope.editMessageBody = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var message = row.entity;
        if (message.messageType.name == 'Incident' || message.messageType.name == 'Trackwork') {
            //set the row to retrieve message title on the page
            var viewMessageBodyURI = MESSAGEBODY_VIEW_URI + message.notificationId;
            baseDataService.getBaseData(viewMessageBodyURI).then(function(response){
                var messageChannelForm = response.data;
                messageChannelForm.name = message.heading;
                messageChannelForm.iimsNumber = message.iimsNumber;
                baseDataService.setRow(messageChannelForm);
                //redirect to the incident page.
                $state.go('dashboard.viewMessageBody');
            });
        }
    }

}
