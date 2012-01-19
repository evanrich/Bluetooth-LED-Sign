Imports System.IO.Ports

Module Functions
    Function AddCom2Combo(ByVal cbPort As ComboBox)
        ' Get a list of serial port names.
        Dim ports As String() = SerialPort.GetPortNames()
        Array.Sort(ports)
        ' Show a label with Action information on it
        cbPort.Text = "The following serial ports were found:"
        ' Put each port name Into a comboBox control.
        Dim port As String
        Dim letters As String = "abcdefghijklmnopqrstuvwxyz"
        Dim at As Integer
        Dim anyOf As Char() = letters.ToCharArray()

        For Each port In ports
            'start = port.IndexOfAny(anyOf)
            at = port.LastIndexOfAny(anyOf)
            If at > -1 Then
                cbPort.Items.Add(port.Substring(0, at))
            Else
                cbPort.Items.Add(port)
            End If

        Next port
        ' Select the first item in the combo control

        cbPort.SelectedIndex = 0
    End Function
End Module
