/**
 * Created by arash on 14/08/2015.
 */
function productCtrl($scope, $state, UserService, baseDataService, SUCCESS, FAILURE, PRODUCT_ADD_URI, PRODUCT_STATUS_URI, PRODUCT_TYPE_URI, UNOM_ALL_URI, TAXRUL_ALL_URI, SUPPLIER_ALL_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.productForm = {};
            $scope.productForm.prodId = -1;
            $scope.productForm.prouId = -1;
        } else {
            $scope.productForm = angular.copy(baseDataService.getRow());
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
        }
        baseDataService.getBaseData(PRODUCT_STATUS_URI).then(function(response){
            $scope.productStatusSet = response.data;
            $scope.productForm.status = baseDataService.populateSelectList($scope.productForm.status,$scope.productStatusSet);
        });
        baseDataService.getBaseData(PRODUCT_TYPE_URI).then(function(response){
            $scope.productTypeSet = response.data;
            $scope.productForm.prodType = baseDataService.populateSelectList($scope.productForm.prodType,$scope.productTypeSet);
        });
        baseDataService.getBaseData(TAXRUL_ALL_URI).then(function(response){
            $scope.taxRuleSet = response.data;
            $scope.productForm.taxRules = baseDataService.populateMultiSelectList($scope.productForm.taxRules,$scope.taxRuleSet);
        });
        baseDataService.getBaseData(UNOM_ALL_URI).then(function(response){
            $scope.unitOfMeasureSet = response.data;
            $scope.suppUnitOfMeasure = baseDataService.populateSelectList($scope.suppUnitOfMeasure,$scope.unitOfMeasureSet);
        });
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
            $scope.supplier = baseDataService.populateSelectList($scope.supplier,$scope.supplierSet);
        });
    }

    function initSupplierPriceGrid() {
        $scope.gridOptions = {
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'solId', visible:false, enableCellEdit:false},
                {field:'prodId', visible:false, enableCellEdit:false},
                {field:'catalogueNo',enableCellEdit:false},
                {field:'partNo', enableCellEdit:false},
                {field:'unitOfMeasure', enableCellEdit:false},
                {field:'unomQty', enableCellEdit:false},
                {field:'price', enableCellEdit:false},
                {field:'bulkQty', enableCellEdit:false},
                {field:'bulkPrice', enableCellEdit:false}
            ]
        }
        $scope.gridOptions.enableRowSelection = false;
        $scope.gridOptions.multiSelect = false;
        $scope.gridOptions.noUnselect= true;

        //
        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
        };

        if ($scope.pageIsNew == false) {
            $scope.gridOptions.data = $scope.productForm.suppProdPrices;
        }
    }

    $scope.addSuppProdPrice= function() {
        suppProdPrice = {
            "id" : -1,
            "solId" : -1,
            "prodId" : -1,
            "catalogueNo" : $scope.suppCatalogueNo,
            "partNo" : $scope.incident.transpositionImpact,
            "unitOfMeasure" : extractedStations,
            "unomQty" : $scope.incident.transpositionImpact,
            "price" : $scope.incident.transpositionImpact,
            "bulkQty" : $scope.incident.transpositionImpact,
            "bulkPrice" : $scope.incident.transpositionImpact,
        }
        $scope.gridOptions.data.push(tranaposition);
    };
    $scope.removeTransposition= function () {
        var selectedRow = baseDataService.getRow();
        rowIndex = getArrIndexOf($scope.gridOptions.data, selectedRow);
        if (rowIndex>-1) {
            $scope.gridOptions.data.splice(rowIndex,1);
            baseDataService.setRowSelected(false);
        }


    };
    function getArrIndexOf(arr,item) {
        if (arr == undefined || item== undefined)
            return -1;
        for (var j = 0; j < arr.length; j++) {
            if (arr[j].tripName == item.tripName && arr[j].impactName == item.impactName && arr[j].stations == item.stations) {
                return j;
            }
        }
        return -1;
    };

    //create new product
    $scope.createProduct = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.productForm;
        baseDataService.addRow(rowObject, PRODUCT_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listProduct');
            } else {
                alert('Not able to save Product. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }
}
