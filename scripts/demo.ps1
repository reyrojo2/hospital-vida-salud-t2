$ErrorActionPreference = 'Stop'
$base = 'http://localhost:8080'
$doc = 'FICT-' + (Get-Random -Minimum 100000 -Maximum 999999)
$pacienteBody = @{
 tipoDocumento='DNI'; numeroDocumento=$doc; nombres='Paciente'; apellidos='Demostracion'
 fechaNacimiento='2000-01-15'; telefono='900000000'; correo='demo@example.test'; direccion='Direccion ficticia'
} | ConvertTo-Json
Write-Host '1. Registrar paciente ficticio'
$p = Invoke-RestMethod -Uri "$base/api/pacientes" -Method Post -ContentType 'application/json' -Body $pacienteBody
$p | ConvertTo-Json
Write-Host '2. Consultar paciente por su identificador'
Invoke-RestMethod -Uri "$base/api/pacientes/$($p.id)" | ConvertTo-Json
Write-Host '3. Intentar duplicado (se espera HTTP 409)'
try {
 Invoke-RestMethod -Uri "$base/api/pacientes" -Method Post -ContentType 'application/json' -Body $pacienteBody | Out-Null
 throw 'ERROR: se acepto un documento duplicado'
} catch {
 if ($_.Exception.Response -and [int]$_.Exception.Response.StatusCode -eq 409) { Write-Host 'OK: HTTP 409' }
 else { throw }
}
$fecha = (Get-Date).ToUniversalTime().AddDays(5).ToString('yyyy-MM-ddTHH:mm:ss')
$citaBody = @{ pacienteId=$p.id; especialidad='Medicina general ficticia'; fechaHora=$fecha; profesionalCodigo='AG-' + $doc; motivoAdministrativo='Reserva administrativa de prueba' } | ConvertTo-Json
Write-Host '4. Registrar cita'
$c = Invoke-RestMethod -Uri "$base/api/citas" -Method Post -ContentType 'application/json' -Body $citaBody
$c | ConvertTo-Json
Write-Host '5. Confirmar cita y publicar evento'
$confirmada = Invoke-RestMethod -Uri "$base/api/citas/$($c.id)/estado" -Method Patch -ContentType 'application/json' -Body '{"estado":"CONFIRMADA"}'
$confirmada | ConvertTo-Json
Write-Host '6. Registrar atencion administrativa'
$atencionBody = @{ pacienteId=$p.id; citaId=$c.id; area='Admisión simulada' } | ConvertTo-Json
Invoke-RestMethod -Uri "$base/api/atenciones" -Method Post -ContentType 'application/json' -Body $atencionBody | ConvertTo-Json
Write-Host '7. Registrar pago simulado'
$pagoBody = @{ pacienteId=$p.id; concepto='Registro administrativo ficticio'; monto=25.50; medioPago='EFECTIVO'; numeroOperacion='OP-' + $doc } | ConvertTo-Json
Invoke-RestMethod -Uri "$base/api/pagos" -Method Post -ContentType 'application/json' -Body $pagoBody | ConvertTo-Json
Start-Sleep -Seconds 2
Write-Host '8. Mostrar notificaciones y auditoria'
Invoke-RestMethod -Uri "$base/api/notificaciones" | ConvertTo-Json -Depth 8
Invoke-RestMethod -Uri "$base/api/auditoria" | ConvertTo-Json -Depth 8
Write-Host 'Demostracion terminada. Verifica RabbitMQ y PostgreSQL para tus capturas.'
