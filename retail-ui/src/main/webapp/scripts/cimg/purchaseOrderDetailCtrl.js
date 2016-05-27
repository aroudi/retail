/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderDetailCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE, POL_CREATION_TYPE_MANUAL, POH_STATUS_IN_PROGRESS, POH_SAVE_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection style ="height: 100px;"></div>',
        expandableRowScope:{
            subGridVariable: 'subGridScopeVariable'
        } ,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false, width:'40%'},
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'10%'},
            {field:'polUnitCost', displayName:'Cost',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polQtyOrdered', displayName:'Qty',enableCellEdit:true, width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polValueOrdered', displayName:'Value',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polCreationType', displayName:'Type',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', sortable:false,enableFiltering:false,enableCellEdit:false, cellTemplate:'<a href=""><i tooltip="delete item" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.removeItem(row)" ></i></a>', width: '5%'}
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
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'polQtyOrdered') {
                $scope.polQtyOrderedBeforeEditting = rowEntity.polQtyOrdered;
            }
        })
    };
    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var purchaseLine = event.targetScope.row.entity;
        cellData = event.targetScope.row.entity[event.targetScope.col.field];
        if (purchaseLine.polCreationType.displayName=='AUTO') {
            if (cellData < getLinkeBoqQtyBalanceTotal(purchaseLine)) {
                baseDataService.displayMessage('Warning!!','You can not decrease quantity for auto created item.');
                purchaseLine.polQtyOrdered = $scope.polQtyOrderedBeforeEditting;
                return;
            }
        }
        //update the total value of the line
        updatePurchaseLineValueOrdered(purchaseLine);
    })

    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.purchaseOrderHeader = {};
            $scope.purchaseOrderHeader.pohCreatedDate = new Date().getTime();
            $scope.purchaseOrderHeader.id = -1;
            $scope.pageIsNew = true;
        } else {
            $scope.purchaseOrderHeader = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.purchaseOrderHeader.lines;
            for (i=0; i<$scope.gridOptions.data.length; i++) {
                displayLinkedBoqs($scope.gridOptions.data[i])
            }
            baseDataService.setRow({});
            baseDataService.setIsPageNew(true);
            $scope.pageIsNew = false;
        }
        baseDataService.getBaseData(POL_CREATION_TYPE_MANUAL).then(function(response){
            var data = angular.copy(response.data);
            $scope.polCreationTypeManual = data;
        });

        if ($scope.pageIsNew) {
            baseDataService.getBaseData(POH_STATUS_IN_PROGRESS).then(function(response){
                var data = angular.copy(response.data);
                $scope.purchaseOrderHeader.pohStatus = data;
            });
        }

    }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

    $scope.searchProduct = function () {
        ngDialog.openConfirm({
            template:'views/pages/productSaleItemSearch.html',
            controller:'productPurchaseItemSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {supplier: function(){return $scope.purchaseOrderHeader.supplier}}
        }).then (function (selectedItems){
                if (selectedItems != undefined) {
                    for (var i = 0; i < selectedItems.length; i++) {
                        var selectedProduct = selectedItems[i];
                        var purchaseLine = createPurchaseLine(selectedProduct);
                        $scope.gridOptions.data.push(purchaseLine);
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    function createPurchaseLine (item) {
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        var purchaseLineObject = {
            'id':rowId,
            'pohId':$scope.purchaseOrderHeader.id,
            'pohOrderNumber':$scope.purchaseOrderHeader.pohOrderNumber,
            'purchaseItem' : item,
            'polUnitCost' : item.price,
            'unitOfMeasure' : item.unitOfMeasure,
            'polQtyOrdered' : 0.00,
            'polValueOrdered' : 0.00,
            'unomContents' : item.unitOfMeasure,
            'polCreationType' :  $scope.polCreationTypeManual
        }
        return purchaseLineObject;
    }

    function getLinkeBoqQtyBalanceTotal(purchaseLine) {
        var boqQtyBalanceTotal = 0.0;
        var boqLinkedList = purchaseLine.poBoqLinks;
        if (boqLinkedList == undefined || boqLinkedList.length < 1) {
            return boqQtyBalanceTotal;
        }
        for (var i = 0; i < boqLinkedList.length; i++) {
            if (boqLinkedList[i].boqQtyBalance != undefined) {
                boqQtyBalanceTotal = boqQtyBalanceTotal + boqLinkedList[i].boqQtyBalance;
            }
        }
        return boqQtyBalanceTotal;

    }

    function updatePurchaseLineValueOrdered(line) {
        if (line == undefined) {
            return;
        }
        line.polValueOrdered = line.polUnitCost * line.polQtyOrdered;
    }
    $scope.savePurchaseOrder = function () {

        /*
         var userId = UserService.getUserId();
         if (userId == undefined || userId == 0) {
         alert('you need to login first');
         $state.go('dashboard.login');
         }
         */

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.purchaseOrderHeader;
        if ($scope.pageIsNew) {
            $scope.purchaseOrderHeader.lines = $scope.gridOptions.data
        } else {
            //remove the subgrid info from data before submitting:
            for (i=0; i<$scope.gridOptions.data.length; i++) {
                if ($scope.gridOptions.data[i].subGridOptions != undefined || $scope.gridOptions.data[i].subGridOptions != null) {
                    delete $scope.gridOptions.data[i].subGridOptions;
                }
            }
        }
        baseDataService.addRow(rowObject, POH_SAVE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                if ($scope.pageIsNew) {
                    baseDataService.displayMessage("Order Number", "Purhcase order saved with number: " + addResponse.info);
                }
                $state.go('dashboard.purchaseOrderList');
            } else {
                alert('Not able to save purchase order. ' + addResponse.message);
            }
        });
        return;
    }

    $scope.removeItem = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }
        if (row.entity.polCreationType.displayName=='AUTO') {
            return;
        }
        if (!confirm('Are you sure you want to delete this item?')) {
            return;
        }
        var rowIndex = baseDataService.getArrIndexOf($scope.purchaseOrderHeader.lines, row.entity);
        if (rowIndex>-1) {
            $scope.purchaseOrderHeader.lines.splice(rowIndex,1);
        }
    }

    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.purchaseOrderHeader.supplier = value;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.formatDate = function(value) {
        return $filter('date')(value, 'medium');
    }

    function displayLinkedBoqs(line) {
        line.subGridOptions = {
            enableRowSelection :false,
            enableColumnResizing: true,
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
                {name:"projectCode", field:"projectCode", displayName:"Project Code", width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.projectCode
                    }
                },
                {name: "boqQtyTotal", field: "boqQtyTotal", displayName:"BOQ Qty Total", width: '10%'},
                {name: "poQtyReceived", field: "poQtyReceived", displayName:"Qty Received", width: '10%'},
                {name: "boqQtyBalance", field: "boqQtyBalance", displayName:"BOQ Qty Balance", width: '10%'},
                {field:'status', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                }
            ],
            data:line.poBoqLinks
        }
    }


});
