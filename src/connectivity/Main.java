package connectivity;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // KELOMPOK 5
        // 1. MUHAMMAD AZMI FAUZI 202143502172
        // 2. Fiscal Ibrahim
        // 3. Erdiansyah 202143502190
        // 4. Bartolomeus Satria

        System.out.println("#===== THE DEALER V =====#");
        menu();
    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        int menu;

        System.out.println("# Pilihan Menu : ");
        System.out.println("1. Lihat Data Barang");
        System.out.println("2. Tambah Data Barang");
        System.out.println("3. Ubah Data Barang");
        System.out.println("4. Hapus Data Barang");

        System.out.print("Pilih Menu : "); menu = scanner.nextInt();
        switch (menu) {
            case 1:
                System.out.println();
                lihatDataBarang();
                menu();
                break;
            case 2:
                System.out.println();
                tambahDataBarang();
                menu();
                break;
            case 3:
                System.out.println();
                ubahDataBarang();
                menu();
                break;
            case 4:
                System.out.println();
                hapusDataBarang();
                System.out.println();
                menu();
                break;
            default:
                System.out.println("Menu Salah!");
                break;
        }
    }

    public static void lihatDataBarang() {
        try {
            // DATABASE CONNECTION
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db\\dealer.db");
            Statement statement = connection.createStatement();

            // LIHAT SEMUA DATA BARANG
            statement.execute("SELECT * FROM barang");
            ResultSet resultSet = statement.getResultSet();

            System.out.println("# Semua Data Barang : ");

            String leftAlignFormat = "| %-3s | %-20s | %-20s | %-5s | %-15s |%n";
            System.out.format("+-----------------------------------------------------------------------------+%n");
            System.out.format(leftAlignFormat, "ID", "Nama Barang", "Harga Barang", "Stok", "Tanggal");
            System.out.format("+-----------------------------------------------------------------------------+%n");
            while(resultSet.next()) {
                System.out.format(leftAlignFormat,
                        resultSet.getString("id"),
                        resultSet.getString("nama"),
                        resultSet.getString("harga"),
                        resultSet.getString("stok"),
                        resultSet.getString("tanggal")
                );
            }
            System.out.format("+-----------------------------------------------------------------------------+%n");
            statement.close();
            System.out.println();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void tambahDataBarang() {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:db\\dealer.db");) {
            // TAMBAH DATA BARANG
            int jumlah,stok;
            Long harga;
            String nama;
            Scanner scanner = new Scanner(System.in);
            String dateToday = dateToday();

            System.out.print("# Tambah Barang Berapa Banyak ? "); jumlah = scanner.nextInt();
            for (int i = 0; i < jumlah; i++) {
                System.out.println("# Barang ke-"+(i+1)+" : ");

                System.out.print("- Nama Barang : "); nama = scanner.next();
                System.out.print("- Harga Barang : "); harga = scanner.nextLong();
                System.out.print("- Stok Barang : "); stok = scanner.nextInt();
                System.out.println("- Tanggal : " + dateToday);
                System.out.println("Data Berhasil Ditambahkan!");
                System.out.println();

                String query = "INSERT INTO barang(nama,harga,stok,tanggal) VALUES(?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nama);
                statement.setLong(2, harga);
                statement.setInt(3, stok);
                statement.setString(4, dateToday);
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void ubahDataBarang() {
        lihatDataBarang();
        Scanner scanner = new Scanner(System.in);
        int idBarang,stok;
        String nama;
        Long harga;
        String dateToday = dateToday();
        System.out.print("Pilih Barang yang ingin diubah : "); idBarang = scanner.nextInt();
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:db\\dealer.db");) {

            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM barang WHERE id = "+idBarang);
            ResultSet resultSet = statement.getResultSet();

            System.out.println("Nama Barang Sebelumnya : " + resultSet.getString("nama"));
            System.out.print("Nama Barang Baru : "); nama = scanner.next();
            System.out.println();
            System.out.println("Harga Barang Sebelumnya : " + resultSet.getString("harga"));
            System.out.print("Harga Barang Baru : "); harga = scanner.nextLong();
            System.out.println();
            System.out.println("Stok Barang Sebelumnya : " + resultSet.getString("stok"));
            System.out.print("Stok Barang Baru : "); stok = scanner.nextInt();
            System.out.println();
            System.out.println("Tanggal : " + dateToday);

            statement.execute("UPDATE barang SET nama='"+nama+"',harga='"+harga+"',stok='"+stok+"' WHERE id="+idBarang);
            System.out.println("Data berhasil diubah!");
            System.out.println();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void hapusDataBarang() {
        lihatDataBarang();
        Scanner scanner = new Scanner(System.in);
        int idBarang,stok;
        String nama;
        Long harga;
        String dateToday = dateToday();
        System.out.print("Pilih Barang yang ingin dihapus : "); idBarang = scanner.nextInt();
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:db\\dealer.db");) {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM barang WHERE id = "+idBarang);
            ResultSet resultSet = statement.getResultSet();
            char option;

            System.out.println("Apakah yakin ingin menghapus data dibawah ini : ");
            System.out.println("- ID : "+resultSet.getString("id"));
            System.out.println("- Nama Barang : "+resultSet.getString("nama"));
            System.out.println("- Harga Barang : "+resultSet.getString("harga"));
            System.out.println("- Stok Barang : "+resultSet.getString("stok"));
            System.out.println("- Tanggal : "+resultSet.getString("tanggal"));
            System.out.print("Yakin menghapus data? (y/n) : "); option = scanner.next().charAt(0);
            statement.close();

            if (option == 'y') {
                Statement statementHapus = connection.createStatement();
                statementHapus.execute("DELETE FROM barang WHERE id="+idBarang);
                System.out.println("Data Berhasil dihapus");
            } else if(option == 'n') {
                menu();
            } else {
                System.out.println("Kesalahan!");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static String dateToday() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM uuuu");
        LocalDate localDate = LocalDate.now();
        String dateToday = dtf.format(localDate);
        return dateToday;
    }
}
