/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseLineBoqDetailCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, purchaseOrderLine, SUCCESS, FAILURE) {

    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        showGridFooter:true,
        columnDefs :[
            {name:"id", field:"id", visible:false},
            {name:"pohId", field:"pohId", visible:false},
            {name:"polId", field:"polId", visible:false},
            {name:"boqId", field:"boqId", visible:false},
            {name:"projectId", field:"projectId", visible:false},
            {name:"boqDetailId", field:"boqDetailId", visible:false},
            {name:"boqName", field:"boqName", displayName:"BOQ Name", width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.boqName
                }
            },
            {name:"projectCode", field:"projectCode", displayName:"Project Code", width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.projectCode
                }
            },
            {name: "boqQtyTotal", field: "boqQtyTotal", displayName:"BOQ Qty Total", width: '10%'},
            {name: "poQtyReceived", enableCellEdit:true, field: "poQtyReceived", displayName:"Qty Received", width: '10%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {name: "boqQtyBalance", field: "boqQtyBalance", displayName:"BOQ Qty Balance", width: '10%'},
            {field:'status', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            }
        ]
    }
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
            $scope.selectedOption = row.entity;
        });
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'poQtyReceived') {
                $scope.poQtyReceivedBeforeEditting = rowEntity.poQtyReceived;
            }
        })
    };
    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var linkedBoqLine = event.targetScope.row.entity;
        cellData = event.targetScope.row.entity[event.targetScope.col.field];
        //check if entered amound for BOQ qty received is grater than BOQ Total qty
        if (cellData > linkedBoqLine.boqQtyTotal) {
            baseDataService.displayMessage('Warning!!','Entered amound is greater than Total required!!!!');
            linkedBoqLine.poQtyReceived = $scope.poQtyReceivedBeforeEditting;
            return;
        }
        //check if summation of all qty received is equal or less than purchase order line qty received.
        var totalBoqLinkedReceived = 0;
        for (i=0; i<$scope.gridOptions.data.length; i++) {
            totalBoqLinkedReceived = totalBoqLinkedReceived + $scope.gridOptions.data[i].poQtyReceived ;
        }
        if (totalBoqLinkedReceived > $scope.purchaseLine.polQtyReceived ) {
            baseDataService.displayMessage('Warning!!','Total Qty received for BOQ is grater than purchase line qty recived!!!!');
            linkedBoqLine.poQtyReceived = $scope.poQtyReceivedBeforeEditting;
            return;
        }
        linkedBoqLine.boqQtyBalance = linkedBoqLine.boqQtyTotal - linkedBoqLine.poQtyReceived;
    })

    populatePageData();
    function populatePageData() {
        $scope.purchaseLine = purchaseOrderLine;
        $scope.gridOptions.data = $scope.purchaseLine.poBoqLinks;
        $scope.poBoqLinksBackup = angular.copy($scope.purchaseLine.poBoqLinks);
    }

    $scope.submit = function () {
        if ($scope.purchaseLine != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.confirm($scope.purchaseLine);
        }
    }

    $scope.cancel = function() {
        $scope.purchaseLine.poBoqLinks = $scope.poBoqLinksBackup
        $scope.closeThisDialog('button');
    }

});
