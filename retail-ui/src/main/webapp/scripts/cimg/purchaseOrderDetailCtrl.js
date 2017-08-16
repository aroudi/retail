/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderDetailCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog,viewMode, $timeout,baseDataService,multiPageService, SUCCESS, FAILURE, POL_CREATION_TYPE_MANUAL, POH_SAVE_URI, POH_STATUS_URI, POH_UPDATE_LINKED_BOQS_URI, POH_STATUS_IN_PROGRESS, POH_EXPORT_PDF, POH_STATUS_CONFIRMED, POH_STATUS_CANCELLED, GET_PURCHASE_ITEM_PER_SUPPLIER_CATALOG_URI, SUPPLIER_ALL_URI, taxCodeSet, TAXLEGVARIANCE_GST_URI, GET_PURCHASE_ITEMS_PER_SUPPLIER_URI, POH_GET_ALL_OUTSTANDING_AND_CONFIRMED_PER_SUPPLIER_URI, GET_PURCHASE_ITEM_PER_ID_URI) {
    $scope.taxLegVarianceSet = taxCodeSet.data;
    $scope.isViewMode = false;
    if (viewMode!=undefined) {
        $scope.isViewMode = viewMode;
    }
    var rowtpl='<div ng-class="{\'brown\':row.entity.polStatus.categoryCode==\'POH_STATUS_GOOD_RECEIVED\'}"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>';
    $scope.gridOptions = {
        enableFiltering: false,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        enableCellEditOnFocus:true,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" style ="height: 100px;"></div>',
        expandableRowScope:{
            subGridVariable: 'subGridScopeVariable'
        } ,
        rowTemplate : rowtpl,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false, width:'26%',enableFiltering:false,
                cellTooltip: function(row,col) {
                    return 'Price:  ' + row.entity.polUnitCost + '\n' + '--------------------------------' + '\n' +
                     'Bulk Quantity:  ' +  row.entity.purchaseItem.bulkQty + ' --> Price:  ' + row.entity.purchaseItem.bulkPrice +'\n' + '\n' +
                     'Bulk Quantity:  ' +  row.entity.purchaseItem.bulkQty2 + ' --> Price:  ' + row.entity.purchaseItem.bulkPrice2 +'\n' + '\n' +
                     'Bulk Quantity:  ' +  row.entity.purchaseItem.bulkQty3 + ' --> Price:  ' + row.entity.purchaseItem.bulkPrice3 +'\n' + '\n' +
                     'Bulk Quantity:  ' +  row.entity.purchaseItem.bulkQty4 + ' --> Price:  ' + row.entity.purchaseItem.bulkPrice4 +'\n' + '\n' +
                     'Bulk Quantity:  ' +  row.entity.purchaseItem.bulkQty5 + ' --> Price:  ' + row.entity.purchaseItem.bulkPrice5 +'\n'
                }
            },
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false,enableFiltering:false, width:'5%'},
            {field:'taxLegVariance.txlvDesc',editType:'dropdown', displayName:'Tax',enableCellEdit:true,width:'8%',enableFiltering:false,
                editableCellTemplate:'<select class="form-control" data-ng-model="row.entity.taxLegVariance" ng-change="grid.appScope.updatePurchaseLineValues(row.entity)" ng-options="tax.txlvDesc for tax in grid.appScope.taxLegVarianceSet" > </select>'
                //cellTemplate:'<select class="form-control" data-ng-model="row.entity.taxLegVariance"  ng-options="tax.txlvDesc for tax in grid.appScope.taxLegVarianceSet" > </select>'
            },
            {field:'polUnitCost', displayName:'Cost',enableCellEdit:true, width:'8%', cellFilter: 'currency',enableFiltering:false,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'polQtyOrdered', displayName:'Qty Ordered',enableCellEdit:true, width:'8%',type: 'number',enableFiltering:false,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'polValueTax', displayName:'Tax value',enableCellEdit:false, width:'7%', enableFiltering:false,cellFilter: 'currency'},
            {field:'polValueOrdered', displayName:'Total',enableCellEdit:false, width:'7%', enableFiltering:false,cellFilter: 'currency'},
            {field:'polQtyReceived', displayName:'Qty Received', enableCellEdit:false,width:'8%',type: 'number',enableFiltering:false,
                cellTooltip: function(row,col) {
                    return row.entity.polFreeText
                }
            },
            {field:'polCreationType', displayName:'Created',enableCellEdit:false, width:'7%', enableFiltering:false,cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'polStatus', displayName:'Status',enableCellEdit:false,enableFiltering:false, width:'8%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', sortable:false,enableFiltering:false,enableCellEdit:false, cellTemplate:'<a href=""><i tooltip="delete item" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeItem(row)"></i></a> <a href=""><i tooltip="update BOQ" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-show="row.entity.polStatus.categoryCode==\'POH_STATUS_GOOD_RECEIVED\' || row.entity.polStatus.categoryCode==\'POH_STATUS_PARTIAL_REC\'" ng-click="grid.appScope.updateBoqQtyRcvd(row)"></i></a>', width: '5%'}
        ]
    }
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        /*
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        */
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'polQtyOrdered') {
                $scope.polQtyOrderedBeforeEditting = rowEntity.polQtyOrdered;
            }
            if (colDef.name == 'polUnitCost') {
                $scope.polUnitCostBeforeEditting = rowEntity.polUnitCost;
            }
        })

        gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'polQtyOrdered') {
                if (rowEntity.polCreationType.displayName=='AUTO') {
                    if (rowEntity.polQtyOrdered < getLinkeBoqQtyBalanceTotal(rowEntity)) {
                        baseDataService.displayMessage('info','Warning!!','You can not decrease quantity for auto created item.');
                        rowEntity.polQtyOrdered = $scope.polQtyOrderedBeforeEditting;
                        return;
                    }
                }
                //calculate bulk price
                rowEntity.polUnitCost = calculateBulkPrice(rowEntity.purchaseItem, rowEntity.polQtyOrdered, rowEntity.polUnitCost);
                //update the total value of the line
                $scope.updatePurchaseLineValues(rowEntity);
            }
            if (colDef.name == 'polUnitCost') {
                if (rowEntity.polUnitCost > rowEntity.purchaseItem.price) {
                    baseDataService.displayMessage('info','Warning!!','The new price is more than item original price which is: ' + rowEntity.purchaseItem.price);
                    rowEntity.polUnitCost = $scope.polUnitCostBeforeEditting;
                    return;
                }
                //update the total value of the line
                $scope.updatePurchaseLineValues(rowEntity);
            }

        })
    };
    /*
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
        updatePurchaseLineValues(purchaseLine);
    })
    */
    initPageData();
    function initPageData() {
        baseDataService.getBaseData(POL_CREATION_TYPE_MANUAL).then(function(response){
            var data = angular.copy(response.data);
            $scope.polCreationTypeManual = data;
        });
        baseDataService.getBaseData(POH_STATUS_IN_PROGRESS).then(function(response){
            var data = angular.copy(response.data);
            $scope.statusOnProgress = data;
        });
        baseDataService.getBaseData(POH_STATUS_CONFIRMED).then(function(response){
            var data = angular.copy(response.data);
            $scope.statusConfirmed = data;
        });
        baseDataService.getBaseData(POH_STATUS_CANCELLED).then(function(response){
            var data = angular.copy(response.data);
            $scope.statusCancelled = data;
        });
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
            /*
            if ($scope.supplierSet.length > 0) {
                var supplier = {
                    "id" : -1,
                    "supplierName" : "Select"
                }
                $scope.supplierSet.unshift(supplier);
            }
            $scope.purchaseOrderHeader.supplier = baseDataService.populateSelectList($scope.purchaseOrderHeader.supplier,$scope.supplierSet);
            */
            //$scope.changeSupplier();
        });
        baseDataService.getBaseData(POH_STATUS_URI).then(function(response){
            $scope.pohStatusSet = response.data;
            /*
             $scope.pohStatusSet = [];
             var arr = response.data;
             //we shouldn't display PARTIAL REC and GOOD RECEIVED
             for (i=0; i< arr.length; i++) {
             if (arr[i].displayName !='PARTIAL REC' && arr[i].displayName !='GOOD RECEIVED' ) {
             $scope.pohStatusSet.push(arr[i]);
             }
             }
             */
            $scope.purchaseOrderHeader.pohStatus = baseDataService.populateSelectList($scope.purchaseOrderHeader.pohStatus,$scope.pohStatusSet);
            managePoStatusDisplay();
        });
        baseDataService.getBaseData(TAXLEGVARIANCE_GST_URI).then(function(response){
            $scope.gstTaxLegVariance = response.data;
        });

        if ( baseDataService.getIsPageNew()) {
            $scope.purchaseOrderHeader = {};
            $scope.purchaseOrderHeader.pohStatus = $scope.statusOnProgress;
            //$scope.purchaseOrderHeader.pohCreatedDate = new Date().getTime();
            $scope.purchaseOrderHeader.pohExpDeliveryStr = new Date();
            $scope.purchaseOrderHeader.pohCreatedDateStr = new Date();
            $scope.purchaseOrderHeader.id = -1;
            $scope.purchaseOrderHeader.costsIncludeTax = false;
            $scope.pageIsNew = true;
        } else {
            $scope.purchaseOrderHeader = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.purchaseOrderHeader.lines;
            $scope.purchaseOrderHeader.pohExpDeliveryStr = new Date($scope.purchaseOrderHeader.pohExpDeliveryStr);
            $scope.purchaseOrderHeader.pohCreatedDateStr = new Date($scope.purchaseOrderHeader.pohCreatedDateStr);

            for (i=0; i<$scope.gridOptions.data.length; i++) {
                if ($scope.gridOptions.data[i].poBoqLinks != undefined && $scope.gridOptions.data[i].poBoqLinks.length > 0 ) {
                    displayLinkedBoqs($scope.gridOptions.data[i]);
                }
                if ($scope.gridOptions.data[i].poSoLinks != undefined && $scope.gridOptions.data[i].poSoLinks.length > 0 ) {
                    displayLinkedSaleOrders($scope.gridOptions.data[i]);
                }
            }
            baseDataService.setRow({});
            baseDataService.setIsPageNew(true);
            if ($scope.purchaseOrderHeader.id === -1){
                $scope.pageIsNew = true;
            } else {
                $scope.pageIsNew = false;
            }
        }

    }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

    $scope.searchProduct = function () {
        var searchStr = $scope.searchByCatalog;
        if ($scope.searchByCatalog === undefined || $scope.searchByCatalog === null || $scope.searchByCatalog === "") {
            searchStr = "@ALL@";
        }
        $scope.searchByCatalog = "";
        if ($scope.purchaseOrderHeader.supplier === undefined || $scope.purchaseOrderHeader.supplier.id === -1) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/productSaleItemSearch.html',
            controller:'productPurchaseItemSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return GET_PURCHASE_ITEMS_PER_SUPPLIER_URI + $scope.purchaseOrderHeader.supplier.id + "/" + searchStr }}
        }).then (function (result){
                if (result === 'NO_RESULT' || result === undefined) {
                    baseDataService.displayMessage('info','Not Found!','Product not found!!!');
                    return;
                }
                if (result != undefined) {
                    for (var i = 0; i < result.length; i++) {
                        var selectedProduct = result[i];
                        /*
                        if (checkIfProductHasBeenSelected(selectedProduct)) {
                            continue;
                        }
                        */
                        baseDataService.getBaseData(GET_PURCHASE_ITEM_PER_ID_URI + selectedProduct.id).then(function(response){
                            var purchaseItem  = response.data;
                            var purchaseLine = createPurchaseLine(purchaseItem);
                            $scope.gridOptions.data.push(purchaseLine);
                        });
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    function createPurchaseLine (item) {
        var rowId;
        var taxLegVar;
        if (item.taxLegVariance == undefined || item.taxLegVariance == null) {
            taxLegVar = $scope.gstTaxLegVariance;
        } else {
            taxLegVar = item.taxLegVariance;
        }
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
            'polCreationType' :  $scope.polCreationTypeManual,
            'polStatus' :  $scope.statusOnProgress,
            'taxLegVariance' :  taxLegVar,
            'polValueTax' : 0.00,
            'polValueGross' : 0.00
            //polStatus
        }
        $scope.updatePurchaseLineValues(purchaseLineObject);
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

    $scope.updatePurchaseLineValues = function(line) {

        if (line == undefined) {
            return;
        }
        var productsValue = line.polUnitCost * line.polQtyOrdered;
        if (line.taxLegVariance != undefined && line.taxLegVariance.txlvAmount != undefined) {
            line.polValueTax = line.polUnitCost * line.taxLegVariance.txlvRate * line.polQtyOrdered;
        } else {
            line.polValueTax = 0.00;
        }
        if ($scope.purchaseOrderHeader.costsIncludeTax) {
            line.polValueOrdered = productsValue;
            line.polValueGross = productsValue*1 - line.polValueTax*1
        }else {
            line.polValueGross = productsValue;
            line.polValueOrdered = line.polValueGross*1 + line.polValueTax*1;
        }
        totalTransaction();
    }

    $scope.savePoAsDraft = function() {
        $scope.purchaseOrderHeader.pohStatus = $scope.statusOnProgress;
        savePurchaseOrder('draft');
    }

    $scope.submitPo = function() {
        $scope.purchaseOrderHeader.pohStatus = $scope.statusConfirmed;
        savePurchaseOrder('submit');
    }

    function savePurchaseOrder(mode) {

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
                    $scope.purchaseOrderHeader.id = addResponse.info;
                    baseDataService.displayMessage("info","Order Number", "Purhcase order saved with number: " + addResponse.info);
                }
                if (mode == 'draft') {
                    $state.go('dashboard.purchaseOrderList');
                } else {
                    exportToPdf();
                    $state.go('dashboard.purchaseOrderList');
                }
            } else {
                alert('Not able to save purchase order. ' + addResponse.message);
            }
        });
    }

    $scope.updateLinkedBoqs = function () {

        /*
         var userId = UserService.getUserId();
         if (userId == undefined || userId == 0) {
         alert('you need to login first');
         $state.go('dashboard.login');
         }
         */

        //$scope.facility.lastModifiedBy = userId;
        $scope.purchaseOrderHeader.lines = $scope.gridOptions.data;
        var rowObject = $scope.purchaseOrderHeader;
        //remove the subgrid info from data before submitting:
        for (i=0; i<$scope.gridOptions.data.length; i++) {
            if ($scope.gridOptions.data[i].subGridOptions != undefined || $scope.gridOptions.data[i].subGridOptions != null) {
                delete $scope.gridOptions.data[i].subGridOptions;
            }
        }
        baseDataService.addRow(rowObject, POH_UPDATE_LINKED_BOQS_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.purchaseOrderList');
            } else {
                baseDataService.displayMessage("info","Error in saving data", addResponse.message);
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
        if ($scope.purchaseOrderHeader.pohStatus.categoryCode != 'POH_STATUS_IN_PROGRESS' && row.entity.id>=0) {
            return;
        }
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item?').then(function(result){
            if (result) {
                row.entity.deleted = true;
                $scope.gridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });
        totalTransaction();
    }

    $scope.changeSupplier = function() {
        if ($scope.purchaseOrderHeader.supplier.contact != undefined && $scope.purchaseOrderHeader.supplier.contact.address1 != undefined) {
            $scope.purchaseOrderHeader.pohDlvAddress = $scope.purchaseOrderHeader.supplier.contact.address1;
        } else {
            $scope.purchaseOrderHeader.pohDlvAddress = '';
        }
        var contactPerson = '';
        if ($scope.purchaseOrderHeader.supplier.contactFirstName != undefined) {
            contactPerson = contactPerson + $scope.purchaseOrderHeader.supplier.contactFirstName;
        }
        if ($scope.purchaseOrderHeader.supplier.contactSurName != undefined) {
            contactPerson = contactPerson + ' ' +$scope.purchaseOrderHeader.supplier.contactSurName;
        }
        $scope.purchaseOrderHeader.pohContactPerson = contactPerson;
        if ($scope.purchaseOrderHeader.supplier.contact!=undefined && $scope.purchaseOrderHeader.supplier.contact.phone != undefined) {
            $scope.purchaseOrderHeader.pohContactPhone = $scope.purchaseOrderHeader.supplier.contact.phone;
        } else {
            $scope.purchaseOrderHeader.pohContactPhone = '';
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
                if ($scope.purchaseOrderHeader.supplier.contact != undefined && $scope.purchaseOrderHeader.supplier.contact.address1 != undefined) {
                    $scope.purchaseOrderHeader.pohDlvAddress = $scope.purchaseOrderHeader.supplier.contact.address1;
                } else {
                    $scope.purchaseOrderHeader.pohDlvAddress = '';
                }
                var contactPerson = '';
                if ($scope.purchaseOrderHeader.supplier.contactFirstName != undefined) {
                    contactPerson = contactPerson + $scope.purchaseOrderHeader.supplier.contactFirstName;
                }
                if ($scope.purchaseOrderHeader.supplier.contactSurName != undefined) {
                    contactPerson = contactPerson + ' ' +$scope.purchaseOrderHeader.supplier.contactSurName;
                }
                $scope.purchaseOrderHeader.pohContactPerson = contactPerson;
                if ($scope.purchaseOrderHeader.supplier.contact!=undefined && $scope.purchaseOrderHeader.supplier.contact.phone != undefined) {
                    $scope.purchaseOrderHeader.pohContactPhone = $scope.purchaseOrderHeader.supplier.contact.phone;
                } else {
                    $scope.purchaseOrderHeader.pohContactPhone = '';
                }
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
            enableRowEdit : true,
            columnDefs :[
                {name:"id", field:"id", visible:false},
                {name:"pohId", field:"pohId", visible:false},
                {name:"polId", field:"polId", visible:false},
                {name:"boqId", field:"boqId", visible:false},
                {name:"projectId", field:"projectId", visible:false},
                {name:"boqDetailId", field:"boqDetailId", visible:false},
                {name:"projectName", field:"projectName", displayName:"Project", width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.projectName
                    }
                },
                {name:"projectCode", field:"projectCode", displayName:"Project No", width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.projectCode
                    }
                },
                {name: "boqQtyTotal", field: "boqQtyTotal", displayName:"BOQ Total", width: '15%'},
                {name: "poQtyReceived", enableCellEdit:true, field: "poQtyReceived", displayName:"Qty Received", width: '10%',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return 'editModeColor'
                    }
                },
                {name: "boqQtyBalance", field: "boqQtyBalance", displayName:"BOQ Balance", width: '15%'},
                {field:'status', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                }
                //{name:'Action', width:'5%',cellTemplate:'<button type="button" ng-show="row.grid.appScope.row.entity.polStatus.categoryCode==\'POH_STATUS_GOOD_RECEIVED\'" class="btn btn-success" ng-click="grid.appScope.updateBoqQtyRcvd(row)" >Update</button>'}
            ],
            data:line.poBoqLinks
        }
    }

    function displayLinkedSaleOrders(line) {
        line.subGridOptions = {
            enableRowSelection :false,
            enableColumnResizing: true,
            enableRowEdit : true,
            columnDefs :[
                {name:"id", field:"id", visible:false},
                {name:"pohId", field:"pohId", visible:false},
                {name:"polId", field:"polId", visible:false},
                {name:"projectId", field:"projectId", visible:false},
                {name:"txdeId", field:"txdeId", visible:false},
                {name:"txhdId", field:"txhdId", displayName:"Sale Order#", visible:true, width:'10%'},
                {name:"projectCode", field:"projectCode", displayName:"Project Code", width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.projectCode
                    }
                },
                {name: "soLineQtyTotal", field: "soLineQtyTotal", displayName:"Sale Order Qty", width: '15%'},
                {name: "poQtyReceived", enableCellEdit:true, field: "poQtyReceived", displayName:"Qty Received", width: '10%',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return 'editModeColor'
                    }
                },
                {name: "soLineQtyBalance", field: "soLineQtyBalance", displayName:"Sale Order Balance", width: '15%'},
                {field:'poSoStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                }
                //{name:'Action', width:'5%',cellTemplate:'<button type="button" ng-show="row.grid.appScope.row.entity.polStatus.categoryCode==\'POH_STATUS_GOOD_RECEIVED\'" class="btn btn-success" ng-click="grid.appScope.updateBoqQtyRcvd(row)" >Update</button>'}
            ],
            data:line.poSoLinks
        }
    }

    $scope.updateBoqQtyRcvd = function (row) {
        var purchaseLine = row.entity;

        ngDialog.openConfirm({
            template:'views/pages/purchaseLineBoqDetail.html',
            controller:'purchaseLineBoqDetailCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {purchaseOrderLine: function(){return purchaseLine}}
        }).then (function (updatedLine){
                if (updatedLine != undefined) {
                    row.entity = updatedLine;
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }

    function exportToPdf () {
        var exportUrl = POH_EXPORT_PDF + $scope.purchaseOrderHeader.id;
        baseDataService.pdfViewer(exportUrl);
    }
    function managePoStatusDisplay(){
        if ($scope.purchaseOrderHeader.pohStatus.categoryCode == 'POH_STATUS_CONFIRMED') {
            $scope.poIsConfirmed = true;
            $scope.poIsCancelled = false;
        }
        if ($scope.purchaseOrderHeader.pohStatus.categoryCode == 'POH_STATUS_CANCELLED') {
            $scope.poIsConfirmed = false;
            $scope.poIsCancelled = true;
        }

    }

    function checkIfProductHasBeenSelected(item) {

        for (var i = 0; i<$scope.gridOptions.data.length; i++) {
            if ($scope.gridOptions.data[i].purchaseItem.prodId == item.prodId) {
                return true;
            }
        }
        return false;
    }

    $scope.searchProductByCatalogNo = function(keyEvent) {
        if (keyEvent.which != 13) {
            return
        }
        if ($scope.purchaseOrderHeader.supplier === undefined) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        $scope.searchProduct();
    }
    function saveAsDraft()
    {
        if ($scope.isViewMode) {
            return;
        }

        $scope.purchaseOrderHeader.lines = $scope.gridOptions.data;
        var pageId;
        if ($scope.pageData === undefined) {
            pageId = -1;
        } else {
            pageId = $scope.pageData.id;
        }
        var txnType = {
            'id' : -1,
            'categoryCode' : 'PAGE_TYPE_PURCHASE',
            'description' :'Purchase Order'
        }
        $scope.pageData = multiPageService.addPage(pageId, txnType, $scope.purchaseOrderHeader.pohOrderNumber===undefined?'':$scope.purchaseOrderHeader.pohOrderNumber,$scope.purchaseOrderHeader);
    }

    var promise;
    (function refresh() {
        saveAsDraft();
        promise = $timeout(refresh, 5000);
    })();

    $scope.$on('$destroy', function () {
        $timeout.cancel(promise);
        promise = undefined;
    });
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

    function totalTransaction() {
        var poLineList =  $scope.gridOptions.data;
        var valueGross = 0.00;
        var valueNett = 0.00;
        var valueTax = 0.00;
        for (var i = 0; i < poLineList.length; i++) {

            if (!poLineList[i].deleted ) {
                valueNett = valueNett*1 + poLineList[i].polValueOrdered*1 ;
                valueGross = valueGross*1 + poLineList[i].polValueGross*1;
                valueTax = valueTax*1 + poLineList[i].polValueTax*1;
            }
        }
        $scope.purchaseOrderHeader.pohValueNett = valueNett;
        $scope.purchaseOrderHeader.pohValueGross = valueGross;
        $scope.purchaseOrderHeader.pohValueTax = valueTax;
    }
    $scope.onTaxIncludeChange = function() {
        var poLineList =  $scope.gridOptions.data;
        for (var i = 0; i < poLineList.length; i++) {
           if (!poLineList[i].deleted ) {
               $scope.updatePurchaseLineValues(poLineList[i]);
            }
        }
    }
    function calculateBulkPrice(purchaseItem, qty, defaultPrice) {
        if (purchaseItem.bulkQty5 > 0 && qty >= purchaseItem.bulkQty5) {
            return purchaseItem.bulkPrice5;
        }
        if (purchaseItem.bulkQty4 > 0 && qty >= purchaseItem.bulkQty4) {
            return purchaseItem.bulkPrice4;

        }
        if (purchaseItem.bulkQty3 > 0 && qty >= purchaseItem.bulkQty3) {
            return purchaseItem.bulkPrice3;

        }
        if (purchaseItem.bulkQty2 > 0 && qty >= purchaseItem.bulkQty2) {
            return purchaseItem.bulkPrice2;

        }
        if (purchaseItem.bulkQty > 0 && qty >= purchaseItem.bulkQty) {
            return purchaseItem.bulkPrice;

        }
        return defaultPrice;
    }

    $scope.mergePurchaseOrder = function () {
        if ($scope.purchaseOrderHeader.supplier === undefined || $scope.purchaseOrderHeader.supplier.id === -1) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        //check if this supplier has outstanding purchase order.
        ngDialog.openConfirm({
            template:'views/pages/purchaseOrderSearch.html',
            controller:'purchaseOrderSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return POH_GET_ALL_OUTSTANDING_AND_CONFIRMED_PER_SUPPLIER_URI + $scope.purchaseOrderHeader.supplier.id}}
        }).then (function (selectedItem){
                if (selectedItem != undefined) {
                    var purchaseOrderHeader = selectedItem;
                    populateDataFromPurchaseOrder(purchaseOrderHeader);
                } else {
                    baseDataService.displayMessage('info','Warning!','No purchase order found for this supplier');
                    return;
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    function populateDataFromPurchaseOrder(purchaseOrder) {
        for (var i = 0; i < purchaseOrder.lines.length; i++) {
            purchaseOrder.lines[i].merged = true;
            if (purchaseOrder.lines[i].poBoqLinks != undefined && purchaseOrder.lines[i].poBoqLinks.length > 0 ) {
                displayLinkedBoqs(purchaseOrder.lines[i]);
            }
            if (purchaseOrder.lines[i].poSoLinks != undefined && purchaseOrder.lines[i].poSoLinks.length > 0 ) {
                displayLinkedSaleOrders(purchaseOrder.lines[i]);
            }
            $scope.gridOptions.data.push(purchaseOrder.lines[i]);
        }
        totalTransaction();
    }

});
