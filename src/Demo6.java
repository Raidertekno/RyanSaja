import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Demo6 {
    public static void main(String[] args) {
        // Membuat frame utama
        JFrame frame = new JFrame("Sewa Mobil Murah Malang");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Membuat panel utama dengan layout GridBagLayout
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 128));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Membuat label dan text field untuk Nama Penyewa
        JTextField namaPenyewaField = addLabelAndTextField(panel, "Nama Penyewa:", 0, gbc);
        JTextField asalField = addLabelAndTextField(panel, "Asal:", 1, gbc);
        JTextField tujuanField = addLabelAndTextField(panel, "Tujuan:", 2, gbc);

        // Membuat label dan combobox untuk Jenis Mobil
        JLabel jenisMobilLabel = new JLabel("Jenis Mobil:");
        jenisMobilLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(jenisMobilLabel, gbc);

        String[] jenisMobilOptions = {"LCGC", "SUV", "MPV", "Sedan"};
        JComboBox<String> jenisMobilComboBox = new JComboBox<>(jenisMobilOptions);
        gbc.gridx = 1;
        panel.add(jenisMobilComboBox, gbc);

        // Membuat label dan text field untuk Harga Sewa
        JTextField hargaSewaField = addLabelAndTextField(panel, "Harga Sewa (Rp):", 4, gbc);

        // Membuat label dan text field untuk Diskon
        JTextField diskonField = addLabelAndTextField(panel, "Diskon (%):", 5, gbc);

        // Membuat tombol Upload Gambar dan label untuk menampilkan gambar
        JButton uploadButton = new JButton("Upload Gambar");
        JLabel imageLabel = new JLabel();
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(uploadButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.add(imageLabel, gbc);

        // Membuat model dan tabel untuk menyimpan data
        String[] columnNames = {"Nama Penyewa", "Asal", "Tujuan", "Jenis Mobil", "Harga Sewa", "Diskon", "Total Biaya", "Gambar"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) {
                    return ImageIcon.class;
                }
                return String.class;
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(100);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(700, 200));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(tableScrollPane, gbc);

        // Membuat tombol Tambah dan Hapus
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 0, 128));
        JButton tambahButton = new JButton("Tambah");
        JButton hapusButton = new JButton("Hapus");
        buttonPanel.add(tambahButton);
        buttonPanel.add(hapusButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Menambahkan action listener untuk tombol Upload Gambar
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    ImageIcon imageIcon = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
                    Image image = imageIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                }
            }
        });

        // Menambahkan action listener untuk tombol Tambah
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namaPenyewa = namaPenyewaField.getText();
                String asal = asalField.getText();
                String tujuan = tujuanField.getText();
                String jenisMobil = (String) jenisMobilComboBox.getSelectedItem();
                String hargaSewaStr = hargaSewaField.getText();
                String diskonStr = diskonField.getText();

                try {
                    double hargaSewa = Double.parseDouble(hargaSewaStr);
                    double diskon = Double.parseDouble(diskonStr);
                    double totalBiaya = hargaSewa - (hargaSewa * diskon / 100);

                    Object[] row = {namaPenyewa, asal, tujuan, jenisMobil, hargaSewaStr, diskonStr, String.format("%.2f", totalBiaya), imageLabel.getIcon()};
                    tableModel.addRow(row);

                    // Membersihkan input field setelah data ditambahkan
                    namaPenyewaField.setText("");
                    asalField.setText("");
                    tujuanField.setText("");
                    hargaSewaField.setText("");
                    diskonField.setText("");
                    imageLabel.setIcon(null);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Harap masukkan angka yang valid untuk Harga Sewa dan Diskon.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Menambahkan action listener untuk tombol Hapus
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris yang ingin dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private static JTextField addLabelAndTextField(JPanel panel, String labelText, int gridy, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        panel.add(label, gbc);

        JTextField textField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(textField, gbc);

        return textField;
    }
}
