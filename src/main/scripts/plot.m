arg_list = argv();
filename = arg_list{1};
file = dlmread(filename, ",", 'A2..Z999');

x = 1:1:length(file);
for i=2:length(file)
    plot(file(i,:));
    hold on
endfor

figure(1);
print("output.png", "-dpng");