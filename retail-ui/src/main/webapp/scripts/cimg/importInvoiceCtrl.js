/**
 * Created by arash on 05/10/2017.
 */
cimgApp.controller('importInvoiceCtrl', function($scope, uiGridConstants, $state,fileUploadService,singleFileUploadService,baseDataService, SUCCESS, FAILURE, INVOICE_IMPORT_URI) {

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
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;



    $scope.fileName ='Import invoice from doors3';
    $scope.fileSet = [
        'Import invoice from doors3'
    ];
    $scope.onFileSelect = function(files) {

        switch ($scope.fileName){

            case 'Import invoice from doors3':
                uploadUrl = INVOICE_IMPORT_URI;
                break;
        }
        fileUploadService.uploadFileToUrl(files, uploadUrl).then(function (response) {
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
