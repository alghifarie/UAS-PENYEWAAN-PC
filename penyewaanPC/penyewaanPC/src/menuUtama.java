
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.Statement;





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class menuUtama extends javax.swing.JFrame {
    /**
     * Creates new form menuUtama
     */
    public Connection getConnection() {
    try {
        String url = "jdbc:mysql://localhost:3306/penyewaan_pc";
        String user = "root";
        String password = "";
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + e.getMessage());
        return null;
    }
}
    
    
 private void formWindowOpened(java.awt.event.WindowEvent evt) {
    loadPenyewaTable();
    loadTableData();
    loadTransaksiTable();
}



private void loadPCTable() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("Nama PC");
    model.addColumn("Spesifikasi");
    model.addColumn("Harga Sewa");
    model.addColumn("Status");

    try (Connection conn = getConnection()) {
        String sql = "SELECT * FROM pc";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_pc"),
                rs.getString("nama_pc"),
                rs.getString("spesifikasi"),
                rs.getString("harga_sewa"),
                rs.getString("status")
            });
        }

        jTable2.setModel(model);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error memuat tabel PC: " + e.getMessage());
    }
}
 
 private void loadNamaPCComboBox() {
    try (Connection conn = getConnection()) {
        String sql = "SELECT nama_pc FROM pc";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        comboPc.removeAllItems();  // Bersihkan isi ComboBox
        while (rs.next()) {
            comboPc.addItem(rs.getString("nama_pc"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error loadNamaPCComboBox: " + e.getMessage());
    }
}

private void loadPenyewaComboBox() {
    try (Connection conn = getConnection()) {
        String sql = "SELECT nama_penyewa FROM penyewa";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        comboPenyewa.removeAllItems();  // Bersihkan isi ComboBox
        while (rs.next()) {
            comboPenyewa.addItem(rs.getString("nama_penyewa"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error loadPenyewaComboBox: " + e.getMessage());
    }
}

    
private void loadPenyewaTable() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("Nama Penyewa");
    model.addColumn("Alamat");
    model.addColumn("Telepon");

    try (Connection conn = getConnection()) {
        String sql = "SELECT * FROM penyewa";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_penyewa"),
                rs.getString("nama_penyewa"),
                rs.getString("alamat"),
                rs.getString("kontak")
            });
        }

        jTable1.setModel(model);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error memuat tabel Penyewa: " + e.getMessage());
    }
}


private void loadTableData() {
    try (Connection conn = getConnection()) {
        String sql = "SELECT * FROM pc";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0); // Hapus data tabel sebelumnya

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_pc"),
                rs.getString("nama_pc"),
                rs.getString("spesifikasi"),
                rs.getDouble("harga_sewa"),
                rs.getString("status")
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
    }
}

private void loadTransaksiTable() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Transaksi");
    model.addColumn("Nama PC");
    model.addColumn("Nama Penyewa");
    model.addColumn("Tanggal Sewa");
    model.addColumn("Tanggal Kembali");
    model.addColumn("Total Harga");

    try (Connection conn = getConnection()) {
        String sql = "SELECT t.id_transaksi, p.nama_pc, py.nama_penyewa, t.tanggal_sewa, t.tanggal_kembali, t.total_harga " +
                     "FROM transaksi t " +
                     "JOIN pc p ON t.id_pc = p.id_pc " +
                     "JOIN penyewa py ON t.id_penyewa = py.id_penyewa";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_transaksi"),
                rs.getString("nama_pc"),
                rs.getString("nama_penyewa"),
                rs.getString("tanggal_sewa"),
                rs.getString("tanggal_kembali"),
                rs.getBigDecimal("total_harga")
            });
        }

        jTable3.setModel(model);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error memuat tabel transaksi: " + e.getMessage());
    }
}




    private void tblPcMouseClicked(java.awt.event.MouseEvent evt) {
    int selectedRow = jTable2.getSelectedRow(); // Mendapatkan baris yang diklik
    if (selectedRow != -1) { // Pastikan ada baris yang diklik
        // Ambil data dari tabel berdasarkan kolom
        String idPc = jTable2.getValueAt(selectedRow, 0).toString(); // Kolom pertama (id_pc)
        String namaPc = jTable2.getValueAt(selectedRow, 1).toString(); // Kolom kedua (nama_pc)
        String spesifikasi = jTable2.getValueAt(selectedRow, 2).toString(); // Kolom ketiga (spesifikasi)
        String hargaSewa = jTable2.getValueAt(selectedRow, 3).toString(); // Kolom keempat (harga_sewa)
        String status = jTable2.getValueAt(selectedRow, 4).toString(); // Kolom kelima (status)

        // Set nilai ke field input
        txtNamaPc.setText(namaPc);
        txtSpesifikasi.setText(spesifikasi);
        txtHargaSewa.setText(hargaSewa);
        comboStatus.setSelectedItem(status); // Set ComboBox sesuai status
    }
}
    

private void clearInputPC() {
    txtNamaPc.setText("");
    txtSpesifikasi.setText("");
    txtHargaSewa.setText("");
    comboStatus.setSelectedIndex(0);
}


    public menuUtama() {
        initComponents();
        loadTransaksiTable();
        loadPCTable();
        loadPenyewaTable();
        loadTableData();
        loadNamaPCComboBox();
        loadPenyewaComboBox();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNamaPenyewa = new javax.swing.JTextField();
        txtKontak = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        btnTambahPenyewa = new javax.swing.JButton();
        btnEditPenyewa = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnHapusPenyewa = new javax.swing.JButton();
        btnCetakPenyewa = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNamaPc = new javax.swing.JTextField();
        txtHargaSewa = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSpesifikasi = new javax.swing.JTextArea();
        btnEditPc = new javax.swing.JButton();
        btnTambahPc = new javax.swing.JButton();
        comboStatus = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        btnHapusPc = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        btnCetakPc = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        btnEditTransaksi = new javax.swing.JButton();
        btnTambahTransaksi = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        txtTotalHarga = new javax.swing.JTextField();
        comboPenyewa = new javax.swing.JComboBox<>();
        comboPc = new javax.swing.JComboBox<>();
        txtTanggalKembali = new javax.swing.JTextField();
        txtTanggalSewa = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        btnHapusTransaksi = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        btnCetakTransaksi = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(18, 53, 36));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(240, 240, 240));
        jLabel1.setText("Form Penyewaan PC");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(507, 507, 507))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(62, 88, 121));

        jPanel3.setBackground(new java.awt.Color(255, 250, 236));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel9.setText("Nama Penyewa");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel10.setText("Kontak");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel12.setText("Alamat");

        txtNamaPenyewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPenyewaActionPerformed(evt);
            }
        });

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        btnTambahPenyewa.setText("Tambah");
        btnTambahPenyewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPenyewaActionPerformed(evt);
            }
        });

        btnEditPenyewa.setText("Edit");
        btnEditPenyewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPenyewaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12))))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtKontak, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                                .addComponent(txtNamaPenyewa))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(btnTambahPenyewa)
                        .addGap(88, 88, 88)
                        .addComponent(btnEditPenyewa)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtNamaPenyewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTambahPenyewa)
                    .addComponent(btnEditPenyewa))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel13.setText("Data Penyewa");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Nama Penyewa", "Kontak", "Alamat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable1);

        btnHapusPenyewa.setText("Hapus");
        btnHapusPenyewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPenyewaActionPerformed(evt);
            }
        });

        btnCetakPenyewa.setText("Cetak");
        btnCetakPenyewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPenyewaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(btnCetakPenyewa, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(btnHapusPenyewa, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapusPenyewa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCetakPenyewa)
                .addContainerGap())
        );

        jLabel15.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel15.setText("Input Data Penyewa");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel3.setText("Nama PC");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel4.setText("Spesifikasi");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel5.setText("Status");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel6.setText("Harga Sewa");

        txtNamaPc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPcActionPerformed(evt);
            }
        });

        txtSpesifikasi.setColumns(20);
        txtSpesifikasi.setRows(5);
        jScrollPane1.setViewportView(txtSpesifikasi);

        btnEditPc.setText("Edit");
        btnEditPc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPcActionPerformed(evt);
            }
        });

        btnTambahPc.setText("Tambah");
        btnTambahPc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPcActionPerformed(evt);
            }
        });

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "tersedia", "disewa" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                            .addComponent(txtHargaSewa)
                            .addComponent(txtNamaPc)
                            .addComponent(comboStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(btnTambahPc)
                        .addGap(77, 77, 77)
                        .addComponent(btnEditPc)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNamaPc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel6))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtHargaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTambahPc)
                    .addComponent(btnEditPc))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        btnHapusPc.setText("Hapus");
        btnHapusPc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPcActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Nama PC", "Spesisfikasi", "Harga Sewa", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable2);

        btnCetakPc.setText("Cetak");
        btnCetakPc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(btnHapusPc)
                        .addGap(223, 223, 223))))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(btnCetakPc, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapusPc)
                .addGap(12, 12, 12)
                .addComponent(btnCetakPc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel40.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel40.setText("Nama PC");

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel41.setText("Penyewa");

        jLabel42.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel42.setText("tanggal kembali");

        jLabel43.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel43.setText("tanggal sewa");

        btnEditTransaksi.setText("Edit");
        btnEditTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditTransaksiActionPerformed(evt);
            }
        });

        btnTambahTransaksi.setText("Tambah");
        btnTambahTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahTransaksiActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel46.setText("total harga");

        txtTotalHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalHargaActionPerformed(evt);
            }
        });

        txtTanggalKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTanggalKembaliActionPerformed(evt);
            }
        });

        txtTanggalSewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTanggalSewaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(btnTambahTransaksi)
                .addGap(48, 48, 48)
                .addComponent(btnEditTransaksi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jLabel42)
                    .addComponent(jLabel46)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(comboPc, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboPenyewa, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTanggalKembali, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTanggalSewa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel43)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel42)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel46))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(comboPc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboPenyewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTanggalSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txtTanggalKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditTransaksi)
                    .addComponent(btnTambahTransaksi))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        btnHapusTransaksi.setText("Hapus");
        btnHapusTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusTransaksiActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama PC", "Penyewa", "Tanggal Sewa", "Tanggal Kembali", "Total Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane12.setViewportView(jTable3);

        btnCetakTransaksi.setText("Cetak");
        btnCetakTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakTransaksiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(234, 234, 234)
                                .addComponent(btnHapusTransaksi))
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(btnCetakTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHapusTransaksi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCetakTransaksi)
                .addContainerGap())
        );

        jLabel39.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel39.setText("Input Data PC");

        jLabel47.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel47.setText("Input Data Transaksi");

        jLabel14.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel14.setText("Data PC");

        jLabel48.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel48.setText("Data Transaksi");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(132, 132, 132)
                                        .addComponent(jLabel47))
                                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(58, 58, 58))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(152, 152, 152)
                                .addComponent(jLabel15))
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addComponent(jLabel39)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addComponent(jLabel13)
                        .addGap(261, 261, 261))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(196, 196, 196)
                                .addComponent(jLabel14))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel48)
                                    .addGap(198, 198, 198))
                                .addComponent(jPanel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(24, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel48)
                        .addGap(13, 13, 13)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 958, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditPcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPcActionPerformed
    int selectedRow = jTable2.getSelectedRow();
    if (selectedRow != -1) { // Pastikan ada baris yang dipilih
        try (Connection conn = getConnection()) {
            String sql = "UPDATE pc SET nama_pc = ?, spesifikasi = ?, harga_sewa = ?, status = ? WHERE id_pc = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNamaPc.getText());
            pst.setString(2, txtSpesifikasi.getText());
            pst.setDouble(3, Double.parseDouble(txtHargaSewa.getText()));
            pst.setString(4, comboStatus.getSelectedItem().toString());
            int idPc = Integer.parseInt(jTable2.getValueAt(selectedRow, 0).toString()); // Ambil id_pc dari tabel
            pst.setInt(5, idPc);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui");

            // Bersihkan inputan
            clearInputPC();
            loadPCTable(); // Refresh tabel PC
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan diperbarui terlebih dahulu.");
    }
    }//GEN-LAST:event_btnEditPcActionPerformed

    private void txtNamaPcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPcActionPerformed

    private void btnEditTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditTransaksiActionPerformed
    int selectedRow = jTable3.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data transaksi yang ingin diubah.");
        return;
    }

    if (txtTanggalSewa.getText().trim().isEmpty() || txtTanggalKembali.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Tanggal sewa dan total harga tidak boleh kosong!");
        return;
    }

    try (Connection conn = getConnection()) {
        String sql = "UPDATE transaksi SET id_pc = ?, id_penyewa = ?, tanggal_sewa = ?, tanggal_kembali = ?, total_harga = ? WHERE id_transaksi = ?";
        PreparedStatement pst = conn.prepareStatement(sql);

        int idTransaksi = Integer.parseInt(jTable3.getValueAt(selectedRow, 0).toString());
        int idPc = Integer.parseInt(comboPc.getSelectedItem().toString().split(" - ")[0]);
        int idPenyewa = Integer.parseInt(comboPenyewa.getSelectedItem().toString().split(" - ")[0]);

        pst.setInt(1, idPc);
        pst.setInt(2, idPenyewa);
        pst.setString(3, txtTanggalSewa.getText());
        pst.setString(4, txtTanggalKembali.getText()); // Gunakan txtTotalHarga1 untuk tanggal_kembali
        pst.setBigDecimal(5, new BigDecimal(txtTotalHarga.getText()));
        pst.setInt(6, idTransaksi);

        int rowsUpdated = pst.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Data transaksi berhasil diubah!");
        } else {
            JOptionPane.showMessageDialog(null, "Data transaksi gagal diubah!");
        }

        loadTransaksiTable(); // Memuat ulang tabel transaksi
        clearInputTransaksi(); // Membersihkan input form
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEditTransaksiActionPerformed

    private void btnEditPenyewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPenyewaActionPerformed
    int selectedRow = jTable1.getSelectedRow(); // Ambil baris yang dipilih
    if (selectedRow != -1) {
        try (Connection conn = getConnection()) {
            // Ambil id_penyewa dari tabel
            int idPenyewa = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());

            // Query SQL untuk update data penyewa
            String sql = "UPDATE penyewa SET nama_penyewa = ?, kontak = ?, alamat = ? WHERE id_penyewa = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            // Mengatur parameter dari inputan
            pst.setString(1, txtNamaPenyewa.getText());
            pst.setString(2, txtKontak.getText());
            pst.setString(3, txtAlamat.getText());
            pst.setInt(4, idPenyewa);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data penyewa berhasil diperbarui!");

            // Bersihkan inputan
            clearInputPenyewa();
            loadPenyewaTable(); // Refresh data tabel
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan diperbarui terlebih dahulu.");
    }
    }//GEN-LAST:event_btnEditPenyewaActionPerformed

    private void txtNamaPenyewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPenyewaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPenyewaActionPerformed

    private void btnHapusPcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPcActionPerformed
    int selectedRow = jTable2.getSelectedRow(); // Mendapatkan baris yang dipilih
    if (selectedRow != -1) { // Pastikan ada baris yang dipilih
        int confirm = JOptionPane.showConfirmDialog(null, 
                "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = getConnection()) {
                // Ambil id_pc dari tabel berdasarkan baris yang dipilih
                int idPc = Integer.parseInt(jTable2.getValueAt(selectedRow, 0).toString()); // Kolom "No"

                // Query SQL untuk menghapus data
                String sql = "DELETE FROM pc WHERE id_pc = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, idPc); // Set parameter id_pc
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");

                // Refresh tabel setelah data dihapus
                loadTableData(); // Pastikan Anda punya fungsi untuk memuat ulang data ke tabel
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus terlebih dahulu.");
    }
    }//GEN-LAST:event_btnHapusPcActionPerformed

    private void btnTambahPenyewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPenyewaActionPerformed
    try (Connection conn = getConnection()) {
        // Query SQL untuk menyimpan data penyewa
        String sql = "INSERT INTO penyewa (nama_penyewa, kontak, alamat) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        // Mengatur parameter dari inputan
        pst.setString(1, txtNamaPenyewa.getText());
        pst.setString(2, txtKontak.getText());
        pst.setString(3, txtAlamat.getText());

        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data penyewa berhasil disimpan!");

        // Bersihkan inputan
        clearInputPenyewa();
        loadPenyewaTable(); // Fungsi untuk refresh data tabel penyewa
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

// Fungsi untuk membersihkan input
private void clearInputPenyewa() {
    txtNamaPenyewa.setText("");
    txtKontak.setText("");
    txtAlamat.setText("");
    }//GEN-LAST:event_btnTambahPenyewaActionPerformed

    private void btnTambahPcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPcActionPerformed
   try (Connection conn = getConnection()) {
        String sql = "INSERT INTO pc (nama_pc, spesifikasi, harga_sewa, status) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        
        pst.setString(1, txtNamaPc.getText());
        pst.setString(2, txtSpesifikasi.getText());
        pst.setDouble(3, Double.parseDouble(txtHargaSewa.getText()));
        pst.setString(4, comboStatus.getSelectedItem().toString());

        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data PC berhasil ditambahkan!");

        // Bersihkan inputan
        clearInputPC();
        loadPCTable(); // Refresh tabel PC
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnTambahPcActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void btnHapusPenyewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPenyewaActionPerformed
     int selectedRow = jTable1.getSelectedRow();
    if (selectedRow != -1) {
        int confirm = JOptionPane.showConfirmDialog(null, 
                "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = getConnection()) {
                int idPenyewa = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
                String sql = "DELETE FROM penyewa WHERE id_penyewa = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, idPenyewa);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data penyewa berhasil dihapus!");

                loadPenyewaTable(); // Refresh data tabel
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus terlebih dahulu.");
    }
    }//GEN-LAST:event_btnHapusPenyewaActionPerformed

    private void btnTambahTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahTransaksiActionPerformed
    if (txtTanggalSewa.getText().trim().isEmpty() || txtTanggalKembali.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Tanggal sewa dan tanggal kembali tidak boleh kosong!");
        return;
    }

    try (Connection conn = getConnection()) {
        String sql = "INSERT INTO transaksi (id_pc, id_penyewa, tanggal_sewa, tanggal_kembali, total_harga) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        int idPc = Integer.parseInt(comboPc.getSelectedItem().toString().split(" - ")[0]);
        int idPenyewa = Integer.parseInt(comboPenyewa.getSelectedItem().toString().split(" - ")[0]);

        String tanggalSewa = txtTanggalSewa.getText(); // VARCHAR format
        String tanggalKembali = txtTanggalKembali.getText(); // VARCHAR format

        pst.setInt(1, idPc);
        pst.setInt(2, idPenyewa);
        pst.setString(3, tanggalSewa);
        pst.setString(4, tanggalKembali);
        pst.setBigDecimal(5, new BigDecimal(txtTotalHarga.getText()));

        int rowsInserted = pst.executeUpdate(); // Tambahkan variabel untuk memeriksa jumlah baris
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Data transaksi berhasil disimpan!");
        } else {
            JOptionPane.showMessageDialog(null, "Data transaksi gagal disimpan!");
        }

        loadTransaksiTable(); // Memuat ulang data tabel
        clearInputTransaksi();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

// Fungsi membersihkan input
private void clearInputTransaksi() {
    comboPc.setSelectedIndex(0);
    comboPenyewa.setSelectedIndex(0);
    txtTanggalSewa.setText("");
    txtTanggalKembali.setText("");
    txtTotalHarga.setText("");
    }//GEN-LAST:event_btnTambahTransaksiActionPerformed

    private void txtTotalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalHargaActionPerformed

    private void btnCetakPcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPcActionPerformed
     try {
        boolean complete = jTable2.print();
        if (complete) {
            JOptionPane.showMessageDialog(null, "Data PC berhasil dicetak.");
        } else {
            JOptionPane.showMessageDialog(null, "Cetak data dibatalkan.");
        }
    } catch (PrinterException e) {
        JOptionPane.showMessageDialog(null, "Error mencetak data PC: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCetakPcActionPerformed

    private void btnCetakPenyewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPenyewaActionPerformed
    try {
        boolean complete = jTable1.print();
        if (complete) {
            JOptionPane.showMessageDialog(null, "Data Penyewa berhasil dicetak.");
        } else {
            JOptionPane.showMessageDialog(null, "Cetak data dibatalkan.");
        }
    } catch (PrinterException e) {
        JOptionPane.showMessageDialog(null, "Error mencetak data Penyewa: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCetakPenyewaActionPerformed

    private void btnCetakTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakTransaksiActionPerformed
    try {
        boolean complete = jTable3.print();
        if (complete) {
            JOptionPane.showMessageDialog(null, "Data transaksi berhasil dicetak.");
        } else {
            JOptionPane.showMessageDialog(null, "Cetak data dibatalkan.");
        }
    } catch (PrinterException e) {
        JOptionPane.showMessageDialog(null, "Error mencetak data: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCetakTransaksiActionPerformed

    private void btnHapusTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusTransaksiActionPerformed
    int selectedRow = jTable3.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus.");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            int idTransaksi = Integer.parseInt(jTable3.getValueAt(selectedRow, 0).toString());

            pst.setInt(1, idTransaksi);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data transaksi berhasil dihapus!");
            loadTransaksiTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btnHapusTransaksiActionPerformed

    private void txtTanggalKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalKembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTanggalKembaliActionPerformed

    private void txtTanggalSewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalSewaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTanggalSewaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menuUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakPc;
    private javax.swing.JButton btnCetakPenyewa;
    private javax.swing.JButton btnCetakTransaksi;
    private javax.swing.JButton btnEditPc;
    private javax.swing.JButton btnEditPenyewa;
    private javax.swing.JButton btnEditTransaksi;
    private javax.swing.JButton btnHapusPc;
    private javax.swing.JButton btnHapusPenyewa;
    private javax.swing.JButton btnHapusTransaksi;
    private javax.swing.JButton btnTambahPc;
    private javax.swing.JButton btnTambahPenyewa;
    private javax.swing.JButton btnTambahTransaksi;
    private javax.swing.JComboBox<String> comboPc;
    private javax.swing.JComboBox<String> comboPenyewa;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtHargaSewa;
    private javax.swing.JTextField txtKontak;
    private javax.swing.JTextField txtNamaPc;
    private javax.swing.JTextField txtNamaPenyewa;
    private javax.swing.JTextArea txtSpesifikasi;
    private javax.swing.JTextField txtTanggalKembali;
    private javax.swing.JTextField txtTanggalSewa;
    private javax.swing.JTextField txtTotalHarga;
    // End of variables declaration//GEN-END:variables
}
