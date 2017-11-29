/**
 * Created by arash on 05/10/2017.
 */
cimgApp.controller('importCtrl', function($scope, uiGridConstants, $state, importEntity, fileUploadService,singleFileUploadService,baseDataService, SUCCESS, FAILURE, INVOICE_IMPORT_URI, SUPPLIER_IMPORT_CSV_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'message', displayName:'Message'}
        ]
    };
    $scope.fileName ='Import invoice from doors3(xml)';
    $scope.fileSet = [
        'Import invoice from doors3(xml)',
        'Import supplier from MYOB(csv)'
    ];
    $scope.title = "Import Invoice";
    var importUrl = INVOICE_IMPORT_URI;
    switch (importEntity) {
        case 'invoice' :
            $scope.title = "Import Invoice";
            $scope.fileName ='Import invoice from doors3(xml)';
            importUrl = INVOICE_IMPORT_URI;
            break;
        case 'supplier' :
            $scope.title = "Import Supplier";
            $scope.fileName ='Import supplier from MYOB(csv)';
            importUrl = SUPPLIER_IMPORT_CSV_URI;
            break;
        case 'client' :
            $scope.title = "Import Client";
            $scope.fileName ='Import client from MYOB(csv)';
            //importUrl = CLIENT_IMPORT_URI;
            break;
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;



    $scope.onFileSelect = function(files) {

        fileUploadService.uploadFileToUrl(files, importUrl).then(function (response) {
            serviceResponse = response.data;
            var responseList=[];
            serviceResponse.messageList.forEach(function (entry) {
                var row = [];
                row.message = entry;
                responseList.push(row)
            });
            $scope.gridOptions.data = responseList;
        });
    };
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

});
